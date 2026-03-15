package tiger.service.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.OperationType;
import tiger.model.requests.account.CreateAccountRequest;
import tiger.model.requests.category.CreateCategoryRequest;
import tiger.model.requests.operation.AddOperationRequest;
import tiger.model.requests.report.GroupedReportRequest;
import tiger.model.requests.report.PeriodicReportRequest;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.BankAccountService;
import tiger.service.CategoryService;
import tiger.service.OperationService;
import tiger.service.ReportService;
import tiger.service.command.CommandExecutor;
import tiger.service.command.report.GroupedReportCommand;
import tiger.service.command.report.PeriodicReportCommand;
import tiger.service.factory.impl.BankAccountFactoryImpl;
import tiger.service.factory.impl.CategoryFactoryImpl;
import tiger.service.factory.impl.OperationFactoryImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportIntegrationTest {
    private static final String MAIN_ACC_NAME = "Основной счет";
    private static final BigDecimal MAIN_ACC_BALANCE = new BigDecimal("100000.00");
    private static final BigDecimal INCOME_AMOUNT = new BigDecimal("50000.00");
    private static final BigDecimal EXPENSE_AMOUNT = new BigDecimal("20000.00");
    private static final BigDecimal DIFF_EXPECTED = new BigDecimal("30000.00");

    private ReportFacade reportFacade;
    private OperationFacade operationFacade;
    private CategoryFacade categoryFacade;
    private CommandExecutor executor;

    private int accId;
    private int incomeCatId;
    private int expenseCatId;

    private static final LocalDateTime START_DATE =
            LocalDateTime.of(2026, 2, 1, 0, 0);
    private static final LocalDateTime END_DATE =
            LocalDateTime.of(2026, 2, 28, 23, 59);

    @BeforeEach
    void setUp() {
        BankAccountRepository accountRepository = new BankAccountRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        OperationRepository operationRepository = new OperationRepository();

        BankAccountFacade accountFacade = new BankAccountFacade(new BankAccountService(accountRepository, new BankAccountFactoryImpl()));
        this.categoryFacade = new CategoryFacade(new CategoryService(categoryRepository, new CategoryFactoryImpl()));
        this.operationFacade = new OperationFacade(new OperationService(operationRepository, accountRepository, categoryRepository, new OperationFactoryImpl()));
        this.reportFacade = new ReportFacade(new ReportService(categoryRepository, operationRepository));
        this.executor = new CommandExecutor();

        accountFacade.create(new CreateAccountRequest(MAIN_ACC_NAME, MAIN_ACC_BALANCE));
        accId = accountFacade.getAll().iterator().next().id();

        categoryFacade.create(new CreateCategoryRequest("Зарплата", OperationType.INCOME));
        incomeCatId = categoryFacade.getAll().stream()
                .filter(c -> c.name().equals("Зарплата")).findFirst().get().id();

        categoryFacade.create(new CreateCategoryRequest("Покупки", OperationType.EXPENSE));
        expenseCatId = categoryFacade.getAll().stream()
                .filter(c -> c.name().equals("Покупки")).findFirst().get().id();
    }

    @Test
    void testReportDiffForSelectedPeriod() {
        operationFacade.create(new AddOperationRequest(
                OperationType.INCOME, accId, INCOME_AMOUNT, START_DATE.plusDays(1), "В отчете", incomeCatId));
        operationFacade.create(new AddOperationRequest(
                OperationType.EXPENSE, accId, EXPENSE_AMOUNT, START_DATE.plusDays(2), "В отчете", expenseCatId));

        operationFacade.create(new AddOperationRequest(
                OperationType.INCOME, accId, INCOME_AMOUNT, START_DATE.minusDays(1), "Вне отчета", incomeCatId));

        var request = new PeriodicReportRequest(accId, START_DATE, END_DATE);
        var command = new PeriodicReportCommand(reportFacade, request);
        executor.execute(command);

        var report = command.getResult();

        assertEquals(0, INCOME_AMOUNT.compareTo(report.totalIncome()));
        assertEquals(0, EXPENSE_AMOUNT.compareTo(report.totalExpense()));
        assertEquals(0, DIFF_EXPECTED.compareTo(report.difference()));
        assertEquals(START_DATE, report.from());
        assertEquals(END_DATE, report.to());
    }

    @Test
    void testReportGroupedCategories() {
        categoryFacade.create(new CreateCategoryRequest("Транспорт", OperationType.EXPENSE));
        int transportId = categoryFacade.getAll().stream()
                .filter(c -> c.name().equals("Транспорт")).findFirst().get().id();

        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accId, EXPENSE_AMOUNT, LocalDateTime.now(), "", expenseCatId));
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accId, EXPENSE_AMOUNT, LocalDateTime.now(), "", expenseCatId));
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accId, EXPENSE_AMOUNT, LocalDateTime.now(), "", transportId));

        var request = new GroupedReportRequest(accId, OperationType.EXPENSE);
        var command = new GroupedReportCommand(reportFacade, request);
        executor.execute(command);

        var report = command.getResult();

        var sum = new BigDecimal(String.valueOf(EXPENSE_AMOUNT));
        sum = sum.add(EXPENSE_AMOUNT);

        assertEquals(0, sum.compareTo(report.categoryTotals().get("Покупки")));
        assertEquals(0, EXPENSE_AMOUNT.compareTo(report.categoryTotals().get("Транспорт")));
    }

    @Test
    void testPeriodicReportThrowsExceptionForInvalidPeriod() {
        var request = new PeriodicReportRequest(accId, LocalDateTime.now(), LocalDateTime.now().minusDays(1));
        var command = new PeriodicReportCommand(reportFacade, request);

        assertThrows(RuntimeException.class, () -> executor.execute(command));
    }
}

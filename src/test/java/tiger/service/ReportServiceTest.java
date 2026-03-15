package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.factory.impl.OperationFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {
    private static final int ACCOUNT_ID = 1;
    private static final int CAT_INCOME_ID = 1;
    private static final int CAT_EXPENSE_ID = 2;
    private static final int CAT_AUTO_ID = 3;

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("100000.00");
    private static final BigDecimal INCOME_AMOUNT = new BigDecimal("50000.00");
    private static final BigDecimal EXPENSE_AMOUNT = new BigDecimal("20000.00");
    private static final BigDecimal DIFF_EXPECTED = new BigDecimal("30000.00");

    private static final LocalDateTime START_DATE = LocalDateTime.of(2026, 2, 1, 0, 0);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2026, 2, 28, 23, 59);

    private ReportService reportService;
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        BankAccountRepository accountRepo = new BankAccountRepository();
        CategoryRepository categoryRepo = new CategoryRepository();
        OperationRepository operationRepo = new OperationRepository();

        reportService = new ReportService(categoryRepo, operationRepo);
        operationService = new OperationService(
                operationRepo, accountRepo, categoryRepo, new OperationFactoryImpl()
        );

        accountRepo.add(new BankAccount(ACCOUNT_ID, "Тестовый счет", INITIAL_BALANCE));

        categoryRepo.add(new Category(CAT_INCOME_ID, "Зарплата", OperationType.INCOME));
        categoryRepo.add(new Category(CAT_EXPENSE_ID, "Покупки", OperationType.EXPENSE));
        categoryRepo.add(new Category(CAT_AUTO_ID, "Авто", OperationType.EXPENSE));
    }

    @Test
    void testReportDiffForSelectedPeriod() {
        operationService.addOperation(
                OperationType.INCOME, ACCOUNT_ID, INCOME_AMOUNT, START_DATE.plusDays(5), "В отчете", CAT_INCOME_ID);
        operationService.addOperation(
                OperationType.EXPENSE, ACCOUNT_ID, EXPENSE_AMOUNT, END_DATE.minusDays(1), "В отчете", CAT_EXPENSE_ID);

        operationService.addOperation(
                OperationType.INCOME, ACCOUNT_ID, INCOME_AMOUNT, START_DATE.minusDays(1), "Вне отчета", CAT_INCOME_ID);
        operationService.addOperation(
                OperationType.EXPENSE, ACCOUNT_ID, EXPENSE_AMOUNT, END_DATE.plusDays(1), "Вне отчета", CAT_EXPENSE_ID);

        var report = reportService.reportDiffForSelectedPeriod(ACCOUNT_ID, START_DATE, END_DATE);

        assertEquals(0, INCOME_AMOUNT.compareTo(report.totalIncome()));
        assertEquals(0, EXPENSE_AMOUNT.compareTo(report.totalExpense()));
        assertEquals(0, DIFF_EXPECTED.compareTo(report.difference()));
        assertEquals(START_DATE, report.from());
        assertEquals(END_DATE, report.to());
    }

    @Test
    void testReportGroupedCategories() {
        operationService.addOperation(OperationType.EXPENSE, ACCOUNT_ID, EXPENSE_AMOUNT, LocalDateTime.now(), "", CAT_EXPENSE_ID);
        operationService.addOperation(OperationType.EXPENSE, ACCOUNT_ID, EXPENSE_AMOUNT, LocalDateTime.now(), "", CAT_EXPENSE_ID);
        operationService.addOperation(OperationType.EXPENSE, ACCOUNT_ID, EXPENSE_AMOUNT, LocalDateTime.now(), "", CAT_AUTO_ID);

        var report = reportService.reportGroupedCategories(ACCOUNT_ID, OperationType.EXPENSE);
        Map<String, BigDecimal> totals = report.categoryTotals();

        var sum = new BigDecimal(String.valueOf(EXPENSE_AMOUNT));
        sum = sum.add(EXPENSE_AMOUNT);

        assertEquals(0, sum.compareTo(totals.get("Покупки")));
        assertEquals(0, EXPENSE_AMOUNT.compareTo(totals.get("Авто")));
        assertEquals(2, totals.size());
    }

    @Test
    void testReportWithInvalidDatesThrowsException() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.minusDays(1);
        assertThrows(IllegalArgumentException.class, () ->
                reportService.reportDiffForSelectedPeriod(ACCOUNT_ID, from, to));
    }

    @Test
    void testReportGroupedCategoriesNoDataReturnsEmptyMap() {
        var report = reportService.reportGroupedCategories(ACCOUNT_ID, OperationType.INCOME);
        assertTrue(report.categoryTotals().isEmpty());
        assertEquals(OperationType.INCOME, report.type());
    }
}

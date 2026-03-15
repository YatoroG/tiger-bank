package tiger.service.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.OperationType;
import tiger.model.dto.OperationFields;
import tiger.model.requests.account.CreateAccountRequest;
import tiger.model.requests.account.GetAccountRequest;
import tiger.model.requests.category.CreateCategoryRequest;
import tiger.model.requests.operation.*;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.BankAccountService;
import tiger.service.CategoryService;
import tiger.service.OperationService;
import tiger.service.command.CommandExecutor;
import tiger.service.command.operation.*;
import tiger.service.factory.impl.BankAccountFactoryImpl;
import tiger.service.factory.impl.CategoryFactoryImpl;
import tiger.service.factory.impl.OperationFactoryImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OperationIntegrationTest {
    private OperationFacade operationFacade;
    private BankAccountFacade accountFacade;
    private CommandExecutor executor;

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("100000.00");
    private static final BigDecimal EXPENSE_AMOUNT = new BigDecimal("20000.00");
    private static final BigDecimal EXPENSE_AMOUNT_NEW = new BigDecimal("20000.00");
    private static final String DESCRIPTION = "Еда";

    private int accountId;
    private int categoryId;

    @BeforeEach
    void setUp() {
        BankAccountRepository accountRepository = new BankAccountRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        OperationRepository operationRepository = new OperationRepository();
        OperationFactoryImpl factory = new OperationFactoryImpl();

        var accountService = new BankAccountService(accountRepository, new BankAccountFactoryImpl());
        var categoryService = new CategoryService(categoryRepository, new CategoryFactoryImpl());
        var operationService = new OperationService(operationRepository, accountRepository, categoryRepository, factory);

        this.accountFacade = new BankAccountFacade(accountService);
        CategoryFacade categoryFacade = new CategoryFacade(categoryService);
        this.operationFacade = new OperationFacade(operationService);
        this.executor = new CommandExecutor();

        accountFacade.create(new CreateAccountRequest("Счет", INITIAL_BALANCE));
        categoryFacade.create(new CreateCategoryRequest("Покупка", OperationType.EXPENSE));
        categoryFacade.create(new CreateCategoryRequest("Штраф", OperationType.EXPENSE));

        accountId = accountFacade.getAll().iterator().next().id();
        categoryId = categoryFacade.getAll().iterator().next().id();
    }

    @Test
    void testAddOperationUpdatesBalance() {
        var req = new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, accountId);
        executor.execute(new AddOperationCommand(operationFacade, req));
        assertEquals(INITIAL_BALANCE.subtract(EXPENSE_AMOUNT),
                accountFacade.getAccount(new GetAccountRequest(accountId)).balance());
    }

    @Test
    void testUpdateAmountRecalculatesBalance() {
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, categoryId));
        int opId = operationFacade.getAll().iterator().next().id();
        executor.execute(new UpdateOperationAmountCommand(operationFacade,
                new UpdateOperationAmountRequest(opId, EXPENSE_AMOUNT_NEW)));
        assertEquals(INITIAL_BALANCE.subtract(EXPENSE_AMOUNT_NEW),
                accountFacade.getAccount(new GetAccountRequest(accountId)).balance());
    }

    @Test
    void testDeleteOperationRestoresBalance() {
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, categoryId));
        int opId = operationFacade.getAll().iterator().next().id();
        var req = new DeleteOperationRequest(opId);
        executor.execute(new DeleteOperationCommand(operationFacade, req));
        assertEquals(INITIAL_BALANCE,
                accountFacade.getAccount(new GetAccountRequest(accountId)).balance());
    }

    @Test
    void testUpdateDescription() {
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, categoryId));
        int opId = operationFacade.getAll().iterator().next().id();
        var req = new UpdateOperationDescriptionRequest(opId, "Новое описание");
        executor.execute(new UpdateOperationDescriptionCommand(operationFacade, req));
        assertEquals("Новое описание", operationFacade.getOperation(new GetOperationRequest(opId)).description());
    }

    @Test
    void testUpdateCategory() {
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, categoryId));
        int opId = operationFacade.getAll().iterator().next().id();
        var req = new UpdateOperationCategoryRequest(opId, 2);
        executor.execute(new UpdateOperationCategoryCommand(operationFacade, req));
        assertEquals(2, operationFacade.getOperation(new GetOperationRequest(opId)).categoryId());
    }

    @Test
    void testUpdateDate() {
        operationFacade.create(new AddOperationRequest(OperationType.EXPENSE, accountId,
                EXPENSE_AMOUNT, LocalDateTime.now(), DESCRIPTION, categoryId));
        int opId = operationFacade.getAll().iterator().next().id();
        LocalDateTime newDate = LocalDateTime.of(2026, 1, 1, 10, 0);
        var req = new UpdateOperationDateRequest(opId, newDate);
        executor.execute(new UpdateOperationDateCommand(operationFacade, req));
        assertEquals(newDate, operationFacade.getOperation(new GetOperationRequest(opId)).date());
    }

    @Test
    void testGetLastFiveOperations() {
        for (int i = 1; i <= 7; i++) {
            var request = new AddOperationRequest(
                    OperationType.EXPENSE,
                    accountId,
                    new BigDecimal("1000.00"),
                    LocalDateTime.now(),
                    "Трата",
                    categoryId
            );
            operationFacade.create(request);
        }

        var lastFive = operationFacade.getLastFive();

        assertEquals(5, lastFive.size());
        boolean allLargeAmounts = lastFive.stream()
                .map(OperationFields::amount)
                .allMatch(amount -> amount.compareTo(new BigDecimal("2")) > 0);

        assertTrue(allLargeAmounts);
    }
}

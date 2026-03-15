package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

public class OperationServiceTest {
    private OperationService service;
    private BankAccountRepository accountRepository;
    private OperationRepository operationRepository;

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("100000.00");
    private static final BigDecimal INCOME_AMOUNT = new BigDecimal("25000.00");
    private static final BigDecimal EXPENSE_AMOUNT = new BigDecimal("20000.00");

    @BeforeEach
    void setUp() {
        accountRepository = new BankAccountRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        operationRepository = new OperationRepository();
        var factory = new OperationFactoryImpl();

        service = new OperationService(
                operationRepository, accountRepository, categoryRepository, factory);

        accountRepository.add(new BankAccount(1, "Основной счет", INITIAL_BALANCE));
        categoryRepository.add(new Category(1, "Зарплата", OperationType.INCOME));
        categoryRepository.add(new Category(2, "Покупки", OperationType.EXPENSE));
    }

    @Test
    void testAddOperationUpdatesBalance() {
        service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Премия", 1);
        BigDecimal expectedBalance = INITIAL_BALANCE.add(INCOME_AMOUNT);
        assertEquals(0, expectedBalance.compareTo(accountRepository.getAccount(1).getBalance()));
        assertEquals(1, operationRepository.getAllOperations().size());
    }

    @Test
    void testUpdateAmountRecalculatesBalance() {
        var op = service.addOperation(OperationType.EXPENSE, 1, EXPENSE_AMOUNT,
                LocalDateTime.now(), "Техника", 2);
        service.updateAmount(op.getOperationId(), new BigDecimal("40000.00"));
        BigDecimal expectedBalance = new BigDecimal("60000.00");
        assertEquals(0, expectedBalance.compareTo(accountRepository.getAccount(1).getBalance()));
    }

    @Test
    void testDeleteOperationRecalculatesBalance() {
        var op = service.addOperation(OperationType.EXPENSE, 1, new BigDecimal("30000.00"),
                LocalDateTime.now(), "Услуги", 2);
        service.deleteOperation(op.getOperationId());
        assertEquals(0, INITIAL_BALANCE.compareTo(accountRepository.getAccount(1).getBalance()));
        assertThrows(IllegalArgumentException.class, () -> service.getOperation(op.getOperationId()));
    }

    @Test
    void testGetLastFiveOperations() {
        for (int i = 1; i <= 6; i++) {
            service.addOperation(OperationType.INCOME, 1, new BigDecimal("1000.00"),
                    LocalDateTime.now(), "Оп " + i, 1);
        }
        var lastFive = service.getLastFiveOperations();
        assertEquals(5, lastFive.size());
        assertEquals(2, lastFive.iterator().next().id());
    }

    @Test
    void testUpdateCategoryAndDescription() {
        var op = service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Старое описание", 1);
        service.updateDescription(op.getOperationId(), "Новое описание");
        service.updateCategory(op.getOperationId(), 2);
        var updated = service.getOperation(op.getOperationId());
        assertEquals("Новое описание", updated.description());
        assertEquals(2, updated.categoryId());
    }

    @Test
    void testUpdateDate() {
        var op = service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Премия", 1);
        LocalDateTime newDate = LocalDateTime.of(2026, 1, 1, 10, 0);

        service.updateDescription(op.getOperationId(), "Кэшбек");
        service.updateDate(op.getOperationId(), newDate);

        var updated = service.getOperation(op.getOperationId());
        assertEquals("Кэшбек", updated.description());
        assertEquals(newDate, updated.date());
    }

    @Test
    void testGetAllOperations() {
        service.addOperation(OperationType.EXPENSE, 1, EXPENSE_AMOUNT,
                LocalDateTime.now(), "Техника", 2);
        service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Премия", 1);
        var operations = service.getAllOperations();
        assertNotNull(operations);
        assertEquals(2, operations.size());
    }

    @Test
    void testCheckNextId() {
        service.checkNextId(10);
        service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Премия", 1);
        assertNotNull(service.getOperation(11));
    }

    @Test
    void testInvalidAccountOrCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.addOperation(OperationType.INCOME,
                        99, INCOME_AMOUNT, LocalDateTime.now(),
                        "Error", 1));
        assertThrows(IllegalArgumentException.class, () ->
                service.addOperation(OperationType.INCOME,
                        1, INCOME_AMOUNT, LocalDateTime.now(),
                        "Error", 99));
    }

    @Test
    void testUpdateCategoryWithInvalidIdThrowsException() {
        var op = service.addOperation(OperationType.INCOME, 1, INCOME_AMOUNT,
                LocalDateTime.now(), "Премия", 1);
        assertThrows(IllegalArgumentException.class, () ->
                service.updateCategory(op.getOperationId(), 999));
    }
}

package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.factory.IOperationFactory;


import static org.junit.jupiter.api.Assertions.*;

public class OperationServiceTest {
    private OperationService operationService;
    private BankAccountRepository accountRepository;
    private CategoryRepository categoryRepository;
    private OperationRepository operationRepository;
    private IOperationFactory operationFactory;

    @BeforeEach
    void setUp() {
        accountRepository = new BankAccountRepository();
        categoryRepository = new CategoryRepository();
        operationRepository = new OperationRepository();
        operationService = new OperationService(operationRepository, accountRepository, categoryRepository, operationFactory);

        accountRepository.add(new BankAccount(1, "Основной счет", new BigDecimal("100000.00")));
        categoryRepository.add(new Category(1, "Зарплата", OperationType.INCOME));
        categoryRepository.add(new Category(2, "Покупки", OperationType.EXPENSE));
    }

    @Test
    void testAddOperationUpdatesBalance() {
        operationService.addOperation(OperationType.INCOME, 1, new BigDecimal("25000.00"),
                LocalDateTime.now(), "Премия", 1);
        BankAccount account = accountRepository.getAccount(1);
        assertEquals(0, new BigDecimal("125000.00").compareTo(account.getBalance()));
        assertEquals(1, operationRepository.getAllOperations().size());
    }

    @Test
    void testUpdateAmountRecalculatesBalance() {
        Operation op = operationService.addOperation(OperationType.EXPENSE, 1, new BigDecimal("20000.00"),
                LocalDateTime.now(), "Покупка техники", 2);
        operationService.updateAmount(op.getOperationId(), new BigDecimal("40000.00"));
        BankAccount account = accountRepository.getAccount(1);
        assertEquals(0, new BigDecimal("60000.00").compareTo(account.getBalance()));
    }

    @Test
    void testDeleteOperationRecalculatesBalance() {
        Operation op = operationService.addOperation(OperationType.EXPENSE, 1, new BigDecimal("30000.00"),
                LocalDateTime.now(), "Покупка техники", 2);
        operationService.deleteOperation(op.getOperationId());
        BankAccount account = accountRepository.getAccount(1);
        assertEquals(0, new BigDecimal("100000.00").compareTo(account.getBalance()));
        assertThrows(IllegalArgumentException.class, () -> operationService.getOperation(op.getOperationId()));
    }

    @Test
    void testGetLastFiveOperations() {
        for (int i = 0; i < 6; i++) {
            operationService.addOperation(OperationType.INCOME, 1, new BigDecimal("10000.00"),
                    LocalDateTime.now(), "Операция №" + i, 1);
        }
        List<Operation> lastFive = operationService.getLastFiveOperations();
        assertEquals(5, lastFive.size());
        assertEquals(2, lastFive.get(0).getOperationId());
        assertEquals(6, lastFive.get(4).getOperationId());
    }

    @Test
    void testUpdateDate() {
        Operation op = operationService.addOperation(OperationType.INCOME, 1, new BigDecimal("10000.00"),
                LocalDateTime.now(), "Описание", 2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime newDate = LocalDate.parse("10.01.2026", formatter).atStartOfDay();
        operationService.updateDate(op.getOperationId(), newDate);
        Operation updated = operationService.getOperation(op.getOperationId());
        assertEquals(newDate, updated.getDate());
    }

    @Test
    void testUpdateCategoryAndDescription() {
        Operation op = operationService.addOperation(OperationType.INCOME, 1, new BigDecimal("10000.00"),
                LocalDateTime.now(), "Старое описание", 2);
        operationService.updateDescription(op.getOperationId(), "Новое описание");
        operationService.updateCategory(op.getOperationId(), 1);
        Operation updated = operationService.getOperation(op.getOperationId());
        assertEquals("Новое описание", updated.getDescription());
        assertEquals(1, updated.getCategoryId());
    }

    @Test
    void testCheckNextId() {
        operationService.checkNextId(10);
        operationService.addOperation(OperationType.INCOME, 1, new BigDecimal("25000.00"),
                LocalDateTime.now(), "Премия", 1);
        assertNotNull(operationService.getOperation(11));
    }
}

package tiger.service.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.service.factory.impl.OperationFactoryImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperationFactoryTest {
    private OperationFactoryImpl factory;
    int accId, catId = 1;

    @BeforeEach
    void setUp() {
        BankAccountRepository accountRepository = new BankAccountRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        factory = new OperationFactoryImpl();

        accountRepository.add(new BankAccount(1, "Основной счет", new BigDecimal("100000.00")));
        categoryRepository.add(new Category(1, "Покупка", OperationType.EXPENSE));
    }

    @Test
    void testCreateOperationWithNullAmountThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.addOperation(OperationType.EXPENSE,
                        accId, null,
                        LocalDateTime.now(),
                        "Трата",
                        catId)
        );
        assertEquals("Ошибка: Сумма не может быть пустой или меньше 0", exception.getMessage());
    }

    @Test
    void testCreateCategoryWithEmptyNameThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.addOperation(OperationType.EXPENSE,
                        accId,
                        new BigDecimal("1.00").subtract(new BigDecimal("2.00")),
                        LocalDateTime.now(),
                        "Трата",
                        catId)
        );
        assertEquals("Ошибка: Сумма не может быть пустой или меньше 0", exception.getMessage());
    }
}

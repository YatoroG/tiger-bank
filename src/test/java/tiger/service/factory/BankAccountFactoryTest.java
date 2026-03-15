package tiger.service.factory;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.service.factory.impl.BankAccountFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

public class BankAccountFactoryTest {
    private BankAccountFactoryImpl factory;

    @BeforeEach
    void setUp() {
        factory = new BankAccountFactoryImpl();
    }

    @Test
    void testCreateAccountWithNullNameThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createAccount(null, new BigDecimal(10000))
        );
        assertEquals("Ошибка: Название счета не может быть пустым", exception.getMessage());
    }

    @Test
    void testCreateAccountWithEmptyNameThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createAccount("   ", new BigDecimal(10000))
        );
        assertEquals("Ошибка: Название счета не может быть пустым", exception.getMessage());
    }

    @Test
    void testCreateAccountWithEmptyBalance() {
        var account = factory.createAccount("Счет", null);
        assertNotNull(account);
        assertEquals("Счет", account.getName());
        assertEquals(new BigDecimal(0), account.getBalance());
    }
}

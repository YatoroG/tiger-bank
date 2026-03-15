package tiger.service;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.repository.BankAccountRepository;
import tiger.service.factory.impl.BankAccountFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

public class BankAccountServiceTest {
    private static final String MAIN_ACC_NAME = "Основной счет";
    private static final BigDecimal MAIN_ACC_BALANCE = new BigDecimal("50000.00");
    private static final String CREDIT_ACC_NAME = "Кредитный счет";
    private static final BigDecimal CREDIT_ACC_BALANCE = new BigDecimal("20000.00");

    private BankAccountService service;

    @BeforeEach
    void setUp() {
        BankAccountRepository repository = new BankAccountRepository();
        BankAccountFactoryImpl factory = new BankAccountFactoryImpl();
        service = new BankAccountService(repository, factory);
    }

    @Test
    void testCreateAndGetAllAccounts() {
        service.createAccount(MAIN_ACC_NAME, MAIN_ACC_BALANCE);
        service.createAccount(CREDIT_ACC_NAME, CREDIT_ACC_BALANCE);

        var accounts = service.getAllAccounts();
        assertEquals(2, accounts.size());

        var acc1 = service.getAccount(1);
        var acc2 = service.getAccount(2);

        assertNotNull(service.getAccount(1));
        assertEquals(1, acc1.id());
        assertEquals(MAIN_ACC_NAME, acc1.name());
        assertEquals(MAIN_ACC_BALANCE, acc1.balance());

        assertNotNull(service.getAccount(2));
        assertEquals(2, acc2.id());
        assertEquals(CREDIT_ACC_NAME, acc2.name());
        assertEquals(CREDIT_ACC_BALANCE, acc2.balance());
    }

    @Test
    void testUpdateAccountName() {
        service.createAccount(MAIN_ACC_NAME, MAIN_ACC_BALANCE);
        service.updateAccountName(1, "Новое название");
        var account = service.getAccount(1);
        assertEquals("Новое название", account.name());
    }

    @Test
    void testDeleteAccount() {
        service.createAccount(MAIN_ACC_NAME, MAIN_ACC_BALANCE);
        assertNotNull(service.getAccount(1));
        service.deleteAccount(1);
        assertThrows(IllegalArgumentException.class, () -> service.getAccount(1));
        assertEquals(0, service.getAllAccounts().size());
    }

    @Test
    void testGetCategory() {
        service.createAccount(MAIN_ACC_NAME, MAIN_ACC_BALANCE);
        assertNotNull(service.getAccount(1));
    }

    @Test
    void testCheckNextId() {
        service.checkNextId(10);
        service.createAccount(MAIN_ACC_NAME, MAIN_ACC_BALANCE);
        assertNotNull(service.getAccount(11));
    }

    @Test
    void testInvalidDeleteIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.getAccount(1));
        assertThrows(IllegalArgumentException.class, () -> service.deleteAccount(1));
    }
}

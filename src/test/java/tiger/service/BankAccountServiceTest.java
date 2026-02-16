package tiger.service;

import java.math.BigDecimal;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.BankAccount;
import tiger.repository.BankAccountRepository;


import static org.junit.jupiter.api.Assertions.*;

public class BankAccountServiceTest {
    private BankAccountRepository bankAccountRepository;
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        bankAccountRepository = new BankAccountRepository();
        bankAccountService = new BankAccountService(bankAccountRepository);
    }

    @Test
    void testCreateAndGetAllAccounts() {
        bankAccountService.createAccount("Основной счет", new BigDecimal("50000.00"));
        bankAccountService.createAccount("Кредитный счет", new BigDecimal("20000.00"));
        Collection<BankAccount> accounts = bankAccountService.getAllAccounts();
        assertEquals(2, accounts.size());
        assertNotNull(bankAccountService.getAccount(1));
        assertNotNull(bankAccountService.getAccount(2));
    }

    @Test
    void testUpdateAccountName() {
        bankAccountService.createAccount("Основной счет", new BigDecimal("50000.00"));
        assertNotNull(bankAccountService.getAccount(1));
        bankAccountService.updateAccountName(1, "Новое название");
        BankAccount account = bankAccountRepository.getAccount(1);
        assertEquals("Новое название", account.getName());
    }

    @Test
    void testDeleteAccount() {
        bankAccountService.createAccount("Основной счет", new BigDecimal("50000.00"));
        assertNotNull(bankAccountService.getAccount(1));
        bankAccountService.deleteAccount(1);
        assertThrows(IllegalArgumentException.class, () -> bankAccountService.getAccount(1));
        assertEquals(0, bankAccountService.getAllAccounts().size());
    }

    @Test
    void testCheckNextId() {
        bankAccountService.checkNextId(10);
        bankAccountService.createAccount("Основной счет", new BigDecimal("50000.00"));
        assertNotNull(bankAccountService.getAccount(11));
    }
}

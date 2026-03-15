package tiger.service.facade;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.requests.account.CreateAccountRequest;
import tiger.model.requests.account.DeleteAccountRequest;
import tiger.model.requests.account.GetAccountRequest;
import tiger.model.requests.account.UpdateAccountNameRequest;
import tiger.repository.BankAccountRepository;
import tiger.service.BankAccountService;
import tiger.service.command.CommandExecutor;
import tiger.service.command.account.CreateAccountCommand;
import tiger.service.command.account.DeleteAccountCommand;
import tiger.service.command.account.UpdateAccountNameCommand;
import tiger.service.factory.impl.BankAccountFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

public class BankAccountIntegrationTest {
    private final String ACC_NAME = "Счет";
    private final BigDecimal ACC_BALANCE = new BigDecimal(10000);
    private BankAccountFacade facade;
    private CommandExecutor executor;

    @BeforeEach
    void setUp() {
        BankAccountRepository repository = new BankAccountRepository();
        BankAccountFactoryImpl factory = new BankAccountFactoryImpl();
        BankAccountService service= new BankAccountService(repository, factory);

        this.facade = new BankAccountFacade(service);
        this.executor = new CommandExecutor();
    }

    @Test
    void testCreateBankAccountThroughCommand() {
        var request = new CreateAccountRequest(ACC_NAME, ACC_BALANCE);
        var command = new CreateAccountCommand(facade, request);
        executor.execute(command);
        var categories = facade.getAll();
        assertEquals(1, categories.size());
        assertEquals(ACC_NAME, categories.iterator().next().name());
    }

    @Test
    void testUpdateBankAccountName() {
        facade.create(new CreateAccountRequest(ACC_NAME, ACC_BALANCE));
        var accountId = facade.getAll().iterator().next().id();

        var request = new UpdateAccountNameRequest(accountId, "Новое название");
        var command = new UpdateAccountNameCommand(facade, request);
        executor.execute(command);

        var updated = facade.getAccount(new GetAccountRequest(accountId));
        assertEquals("Новое название", updated.name());
    }

    @Test
    void testDeleteBankAccount() {
        facade.create(new CreateAccountRequest(ACC_NAME, ACC_BALANCE));
        var accountId = facade.getAll().iterator().next().id();
        assertFalse(facade.getAll().isEmpty());

        var request = new DeleteAccountRequest(accountId);
        var command = new DeleteAccountCommand(facade, request);
        executor.execute(command);

        assertTrue(facade.getAll().isEmpty());
    }

    @Test
    void testGetBankAccountThrowsExceptionIfNotFound() {
        var request = new GetAccountRequest(999);
        assertThrows(RuntimeException.class, () -> { facade.getAccount(request); });
    }
}

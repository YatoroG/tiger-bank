package tiger.service.facade;

import java.util.Collection;
import org.springframework.stereotype.Component;
import tiger.model.dto.BankAccountFields;
import tiger.model.requests.account.CreateAccountRequest;
import tiger.model.requests.account.DeleteAccountRequest;
import tiger.model.requests.account.GetAccountRequest;
import tiger.model.requests.account.UpdateAccountNameRequest;
import tiger.service.BankAccountService;

@Component
public class BankAccountFacade {
    private final BankAccountService bankAccountService;

    public BankAccountFacade(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    public void create(CreateAccountRequest request) {
        bankAccountService.createAccount(request.name(), request.balance());
    }

    public void updateName(UpdateAccountNameRequest request) {
        bankAccountService.updateAccountName(request.id(), request.newName());
    }

    public void delete(DeleteAccountRequest request) {
        bankAccountService.deleteAccount(request.id());
    }

    public Collection<BankAccountFields> getAll() {
        return bankAccountService.getAllAccounts();
    }

    public BankAccountFields getAccount(GetAccountRequest request) {
        return bankAccountService.getAccount(request.id());
    }
}

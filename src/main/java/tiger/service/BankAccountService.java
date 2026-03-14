package tiger.service;

import java.math.BigDecimal;
import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.model.dto.BankAccountFields;
import tiger.repository.BankAccountRepository;
import tiger.service.factory.IBankAccountFactory;

@Service
public class BankAccountService {
    private final BankAccountRepository repository;
    private final IBankAccountFactory factory;

    public BankAccountService(BankAccountRepository repository, IBankAccountFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public void createAccount(String name, BigDecimal balance) {
        BankAccount account = factory.createAccount(name, balance);
        repository.add(account);
    }

    public void updateAccountName(int id, String newName) {
        BankAccount account = searchAccount(id);
        account.setName(newName);
        repository.update(account);
    }

    public void deleteAccount(int id) {
        if (!repository.hasAccount(id)) {
            throw new IllegalArgumentException("Ошибка: Счет с ID " + id + " не найден");
        }
        repository.delete(id);
    }

    public BankAccountFields getAccount(int id) {
        BankAccount account = searchAccount(id);
        return account.splitAccount();
    }

    public Collection<BankAccountFields> getAllAccounts() {
        return repository.getAllAccounts().values().stream()
                .map(BankAccount::splitAccount).toList();
    }

    public void checkNextId(int maxAccId) {
        factory.checkNextId(maxAccId);
    }

    private BankAccount searchAccount(int id) {
        if (!repository.hasAccount(id)) {
            throw new IllegalArgumentException("Ошибка: Счет с ID " + id + " не найден");
        }
        return repository.getAccount(id);
    }

}

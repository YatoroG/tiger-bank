package tiger.service;

import java.math.BigDecimal;
import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.repository.BankAccountRepository;

@Service
public class BankAccountService {
    private final BankAccountRepository repository;
    private int nextAccountId = 1;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public void createAccount(String name, BigDecimal balance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: Название счета не может быть пустым");
        }

        BankAccount account = (balance == null) ? new BankAccount(nextAccountId++, name)
                : new BankAccount(nextAccountId++, name, balance);
        repository.add(account);
    }

    public void updateAccountName(int id, String newName) {
        BankAccount account = repository.getAccount(id);
        if (account != null) {
            account.setName(newName);
            repository.update(account);
        } else {
            System.out.println("Ошибка: Счет с ID " + id + " не найден");
        }
    }

    public void deleteAccount(int id) {
        if (repository.hasAccount(id)) {
            repository.delete(id);
        } else {
            System.out.println("Ошибка: Счет с ID " + id + " не найден");
        }
    }

    public BankAccount getAccount(int id) {
        return repository.getAccount(id);
    }

    public Collection<BankAccount> getAllAccounts() {
        return repository.getAllAccounts().values();
    }

    public void checkNextId(int maxAccId) {
        this.nextAccountId = maxAccId + 1;
    }
}

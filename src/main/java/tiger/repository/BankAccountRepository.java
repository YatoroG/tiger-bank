package tiger.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import tiger.model.BankAccount;

@Repository
public class BankAccountRepository {
    private final Map<Integer, BankAccount> bankAccounts = new HashMap<>();

    public void add(BankAccount bankAccount) {
        bankAccounts.put(bankAccount.getAccountId(), bankAccount);
    }

    public void update(BankAccount bankAccount) {
        bankAccounts.put(bankAccount.getAccountId(), bankAccount);
    }

    public void delete(int id) {
        bankAccounts.remove(id);
    }

    public boolean hasAccount(int id) {
        return bankAccounts.containsKey(id);
    }

    public BankAccount getAccount(int id) {
        return bankAccounts.get(id);
    }

    public Map<Integer, BankAccount> getAllAccounts() {
        return bankAccounts;
    }
}

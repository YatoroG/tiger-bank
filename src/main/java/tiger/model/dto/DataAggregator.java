package tiger.model.dto;

import java.util.List;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;

public class DataAggregator {
    private List<BankAccount> accounts;
    private List<Category> categories;
    private List<Operation> operations;

    public DataAggregator() {
    }

    public DataAggregator(List<BankAccount> accounts,
                          List<Category> categories,
                          List<Operation> operations) {
        this.accounts = accounts;
        this.categories = categories;
        this.operations = operations;
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}

package tiger.model;

import java.math.BigDecimal;

public class BankAccount {
    private final int accountId;
    private String name;
    private BigDecimal balance;

    public BankAccount(int accountId, String name) {
        this.accountId = accountId;
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public BankAccount(int accountId, String name, BigDecimal balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }
}

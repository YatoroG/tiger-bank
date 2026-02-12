package tiger.model;

import java.math.BigDecimal;

public class BankAccount {
    private static int nextAccountId = 1;
    private final int accountId;
    private String name;
    private BigDecimal balance;

    public BankAccount(String name) {
        this.accountId = nextAccountId;
        this.name = name;
        this.balance = BigDecimal.ZERO;
        ++nextAccountId;
    }

    public BankAccount(String name, BigDecimal balance) {
        this.accountId = nextAccountId;
        this.name = name;
        this.balance = balance;
        ++nextAccountId;
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

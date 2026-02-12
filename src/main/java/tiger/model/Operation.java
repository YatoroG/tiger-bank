package tiger.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Operation {
    private static int nextOperationId = 1;
    private final int operationId;
    private OperationType type;
    private final int bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private int categoryId;

    public Operation(OperationType type, int bankAccountId, BigDecimal amount,
                     String description, int categoryId) {
        this.operationId = nextOperationId;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.description = description;
        this.categoryId = categoryId;
        ++nextOperationId;
    }

    public int getOperationId() {
        return operationId;
    }

    public OperationType getType() {
        return type;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal newAmount) {
        this.amount = newAmount;
    }

    public void setCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

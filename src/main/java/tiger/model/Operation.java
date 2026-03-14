package tiger.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import tiger.model.dto.OperationFields;

public class Operation {
    private int operationId;
    private OperationType type;
    private int bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private int categoryId;

    public Operation() {
    }

    public Operation(int operationId, OperationType type, int bankAccountId, BigDecimal amount,
                     LocalDateTime date, String description, int categoryId) {
        this.operationId = operationId;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
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

    public OperationFields splitOperation() {
        return new OperationFields(this.operationId, this.type, this.bankAccountId,
                this.amount, this.date, this.description, this.categoryId);
    }
}

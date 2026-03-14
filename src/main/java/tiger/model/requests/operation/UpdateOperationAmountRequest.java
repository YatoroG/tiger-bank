package tiger.model.requests.operation;

import java.math.BigDecimal;

public record UpdateOperationAmountRequest(int id, BigDecimal newAmount) {
}

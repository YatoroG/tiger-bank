package tiger.model.requests.operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import tiger.model.OperationType;

public record AddOperationRequest(OperationType type, int accountId, BigDecimal amount,
                                  LocalDateTime date, String description, int categoryId) {
}

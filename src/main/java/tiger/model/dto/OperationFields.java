package tiger.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import tiger.model.OperationType;

public record OperationFields(int id, OperationType type,
                              int bankAccountId, BigDecimal amount,
                              LocalDateTime date, String description,
                              int categoryId) {
}

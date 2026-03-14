package tiger.service.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import tiger.model.Operation;
import tiger.model.OperationType;

public interface IOperationFactory {
    Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                           LocalDateTime date, String description, int categoryId);
    void checkNextId(int maxOpId);
}

package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import tiger.model.Operation;
import tiger.model.OperationType;

public interface IOperationService {
    Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                           LocalDateTime date, String description, int categoryId);
    void updateAmount(int id, BigDecimal newAmount);
    void deleteOperation(int id);
    List<Operation> getLastFiveOperations();
}

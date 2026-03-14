package tiger.service.factory.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.service.factory.IOperationFactory;

@Component
public class OperationFactoryImpl implements IOperationFactory {
    private int nextOperationId = 1;

    @Override
    public Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                                  LocalDateTime date, String description, int categoryId) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Ошибка: Сумма не может быть пустой или меньше 0");
        }

        return new Operation(nextOperationId++, type, accountId, amount, date,
                description, categoryId);
    }

    @Override
    public void checkNextId(int maxOpId) {
        this.nextOperationId = maxOpId + 1;
    }
}

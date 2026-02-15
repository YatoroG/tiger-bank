package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tiger.model.Operation;
import tiger.model.OperationType;

@Component
@Primary
public class ProfilingOperationService implements IOperationService {
    private final IOperationService operationService;

    public ProfilingOperationService(@Qualifier("mainOperationService") IOperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                                  LocalDateTime date, String description, int categoryId) {
        long startTime = System.nanoTime();
        Operation result = operationService.addOperation(type, accountId, amount, date,
                description, categoryId);
        System.out.println("[ПРОФИЛИРОВЩИК] Добавление операции: " +
                (System.nanoTime() - startTime) / 1000000.0 + " мс");
        return result;
    }

    @Override
    public void updateAmount(int id, BigDecimal newAmount) {
        long startTime = System.nanoTime();
        operationService.updateAmount(id, newAmount);
        System.out.println("[ПРОФИЛИРОВЩИК] Обновление баланса: " +
                (System.nanoTime() - startTime) / 1000000.0 + " мс");
    }

    @Override
    public void deleteOperation(int id) {
        long startTime = System.nanoTime();
        operationService.deleteOperation(id);
        System.out.println("[ПРОФИЛИРОВЩИК] Удаление операции: " +
                (System.nanoTime() - startTime) / 1000000.0 + " мс");
    }

    @Override
    public List<Operation> getLastFiveOperations() {
        long startTime = System.nanoTime();
        List<Operation> result = operationService.getLastFiveOperations();
        System.out.println("[ПРОФИЛИРОВЩИК] Вывод последних операций: " +
                (System.nanoTime() - startTime) / 1000000.0 + " мс");
        return result;
    }
}

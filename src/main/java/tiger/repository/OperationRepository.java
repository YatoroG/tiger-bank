package tiger.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import tiger.model.Operation;

@Repository
public class OperationRepository {
    private final Map<Integer, Operation> operations = new HashMap<>();

    public void add(Operation operation) {
        operations.put(operation.getOperationId(), operation);
    }

    public void update(Operation operation) {
        operations.put(operation.getOperationId(), operation);
    }

    public void delete(int id) {
        operations.remove(id);
    }

    public boolean hasOperation(int id) {
        return operations.containsKey(id);
    }

    public Operation getOperation(int id) {
        return operations.get(id);
    }

    public Map<Integer, Operation> getAllOperations() {
        return operations;
    }
}

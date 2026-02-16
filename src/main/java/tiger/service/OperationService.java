package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;

@Service("mainOperationService")
public class OperationService implements IOperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private int nextOperationId = 1;

    public OperationService(OperationRepository operationRepository,
                            BankAccountRepository bankAccountRepository,
                            CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                                  LocalDateTime date, String description, int categoryId) {
        BankAccount bankAccount = bankAccountRepository.getAccount(accountId);
        if (bankAccount == null) {
            throw new IllegalArgumentException("Ошибка: Счет с id " + accountId + "не найден");
        }

        Category category = categoryRepository.getCategory(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Ошибка: Категория с " + categoryId + " не найдена");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Ошибка: Сумма не может быть пустой или меньше 0");
        }

        Operation operation = new Operation(nextOperationId++, type, accountId, amount, date,
                description, categoryId);
        operationRepository.add(operation);
        updateBalance(bankAccount, amount, type);
        return operation;
    }

    @Override
    public void updateAmount(int id, BigDecimal newAmount) {
        Operation operation = getOperation(id);
        BankAccount account = bankAccountRepository.getAccount(operation.getBankAccountId());
        updateBalance(account, operation.getAmount(),
                operation.getType() == OperationType.INCOME ? OperationType.EXPENSE : OperationType.INCOME);
        operation.setAmount(newAmount);
        updateBalance(account, newAmount, operation.getType());
        operationRepository.update(operation);
    }

    public void updateCategory(int id, int newCategoryId) {
        if (!categoryRepository.hasCategory(newCategoryId)) {
            throw new IllegalArgumentException("Ошибка: Категория с " + newCategoryId + " не найдена");
        }

        Operation operation = getOperation(id);
        operation.setCategory(newCategoryId);
        operationRepository.update(operation);
    }

    public void updateDescription(int id, String newDescription) {
        Operation operation = getOperation(id);
        operation.setDescription(newDescription);
        operationRepository.update(operation);
    }

    public void updateDate(int id, LocalDateTime newDate) {
        Operation operation = getOperation(id);
        operation.setDate(newDate);
        operationRepository.update(operation);
    }

    @Override
    public void deleteOperation(int id) {
        Operation operation = getOperation(id);
        BankAccount account = bankAccountRepository.getAccount(operation.getBankAccountId());
        updateBalance(account, operation.getAmount(),
                operation.getType() == OperationType.INCOME ? OperationType.EXPENSE : OperationType.INCOME);
        operationRepository.delete(id);
    }

    public Operation getOperation(int id) {
        if (!operationRepository.hasOperation(id)) {
            throw new IllegalArgumentException("Ошибка: Операция с id " + id + " не найдена");
        }
        return operationRepository.getOperation(id);
    }

    public Collection<Operation> getAllOperations() {
        return operationRepository.getAllOperations().values();
    }

    @Override
    public List<Operation> getLastFiveOperations() {
        List<Operation> allOperations = new ArrayList<>(operationRepository.getAllOperations().values());
        int size = allOperations.size();
        return allOperations.subList(Math.max(0, size - 5), size);
    }

    private void updateBalance(BankAccount account, BigDecimal amount, OperationType type) {
        if (type == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }
        bankAccountRepository.update(account);
    }

    public void checkNextId(int maxOpId) {
        this.nextOperationId = maxOpId + 1;
    }
}

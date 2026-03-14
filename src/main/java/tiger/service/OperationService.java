package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.model.dto.OperationFields;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.factory.IOperationFactory;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final IOperationFactory factory;

    public OperationService(OperationRepository operationRepository,
                            BankAccountRepository bankAccountRepository,
                            CategoryRepository categoryRepository,
                            IOperationFactory factory) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.factory = factory;
    }

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

        Operation operation = factory.addOperation(type, accountId, amount, date, description, categoryId);
        operationRepository.add(operation);
        updateBalance(bankAccount, amount, type);
        return operation;
    }

    public void updateAmount(int id, BigDecimal newAmount) {
        Operation operation = searchOperation(id);
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

        Operation operation = searchOperation(id);
        operation.setCategory(newCategoryId);
        operationRepository.update(operation);
    }

    public void updateDescription(int id, String newDescription) {
        Operation operation = searchOperation(id);
        operation.setDescription(newDescription);
        operationRepository.update(operation);
    }

    public void updateDate(int id, LocalDateTime newDate) {
        Operation operation = searchOperation(id);
        operation.setDate(newDate);
        operationRepository.update(operation);
    }

    public void deleteOperation(int id) {
        Operation operation = searchOperation(id);
        BankAccount account = bankAccountRepository.getAccount(operation.getBankAccountId());
        updateBalance(account, operation.getAmount(),
                operation.getType() == OperationType.INCOME ? OperationType.EXPENSE : OperationType.INCOME);
        operationRepository.delete(id);
    }

    public OperationFields getOperation(int id) {
        Operation operation = searchOperation(id);
        return operation.splitOperation();
    }

    public Collection<OperationFields> getAllOperations() {
        return operationRepository.getAllOperations().values().stream()
                .map(Operation::splitOperation).toList();
    }

    public Collection<OperationFields> getLastFiveOperations() {
        return operationRepository.getAllOperations().values().stream()
                .skip(Math.max(0, operationRepository.getAllOperations().size() - 5))
                .map(Operation::splitOperation).toList();
    }

    private void updateBalance(BankAccount account, BigDecimal amount, OperationType type) {
        if (type == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }
        bankAccountRepository.update(account);
    }

    private Operation searchOperation(int id) {
        if (!operationRepository.hasOperation(id)) {
            throw new IllegalArgumentException("Ошибка: Операция с id " + id + " не найдена");
        }
        return operationRepository.getOperation(id);
    }

    public void checkNextId(int maxOpId) {
        factory.checkNextId(maxOpId);
    }
}

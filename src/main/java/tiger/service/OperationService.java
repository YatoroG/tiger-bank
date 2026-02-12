package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    public OperationService(OperationRepository operationRepository,
                            BankAccountRepository bankAccountRepository,
                            CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Operation addOperation(OperationType type, int accountId, BigDecimal amount,
                                  String description, int categoryId) {
        BankAccount bankAccount = bankAccountRepository.getAccount(accountId);
        if (bankAccount == null) {
            throw new IllegalArgumentException("Ошибка: Счет с id " + accountId + "не найден");
        }

        Operation operation = new Operation(type, accountId, amount, description, categoryId);
        operationRepository.add(operation);
        updateBalance(bankAccount, amount, type);
        return operation;
    }

    public void updateAmount(int id, BigDecimal newAmount) {
        Operation operation = operationRepository.getOperation(id);
        if (operation == null) {
            System.out.println("Ошибка: Операция с id " + id + " не найдена");
            return;
        }

        BankAccount account = bankAccountRepository.getAccount(operation.getBankAccountId());
        updateBalance(account, operation.getAmount(),
                operation.getType() == OperationType.INCOME ? OperationType.EXPENSE : OperationType.INCOME);
        operation.setAmount(newAmount);
        updateBalance(account, newAmount, operation.getType());
        operationRepository.update(operation);
    }

    public void updateCategory(int id, int newCategoryId) {
        if (!categoryRepository.hasCategory(newCategoryId)) {
            System.out.println("Ошибка: Категория с id " + newCategoryId + " не найдена");
            return;
        }

        Operation operation = operationRepository.getOperation(id);
        if (operation != null) {
            operation.setCategory(newCategoryId);
            operationRepository.update(operation);
        } else {
            System.out.println("Ошибка: Операция с id " + id + " не найдена");
        }
    }

    public void updateDescription(int id, String newDescr) {
        Operation operation = operationRepository.getOperation(id);
        if (operation != null) {
            operation.setDescription(newDescr);
            operationRepository.update(operation);
        } else {
            System.out.println("Ошибка: Операция с id " + id + " не найдена");
        }
    }

    public void updateDate(int id, LocalDateTime newDate) {
        Operation operation = operationRepository.getOperation(id);
        if (operation != null) {
            operation.setDate(newDate);
            operationRepository.update(operation);
        } else {
            System.out.println("Ошибка: Операция с id " + id + " не найдена");
        }
    }

    public void deleteOperation(int id) {
        Operation operation = operationRepository.getOperation(id);
        if (operation != null) {
            BankAccount account = bankAccountRepository.getAccount(operation.getBankAccountId());
            updateBalance(account, operation.getAmount(),
                    operation.getType() == OperationType.INCOME ?
                            OperationType.EXPENSE : OperationType.INCOME);
            operationRepository.delete(id);
        } else {
            System.out.println("Ошибка: Операция с id " + id + " не найдена");
        }
    }

    public Operation getOperation(int id) {
        return operationRepository.getOperation(id);
    }

    public Collection<Operation> getAllOperations() {
        return operationRepository.getAllOperations().values();
    }

    private void updateBalance(BankAccount account, BigDecimal amount, OperationType type) {
        if (type == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }
        bankAccountRepository.update(account);
    }
}

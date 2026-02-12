package tiger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;

@Service
public class ReportService {
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    public ReportService(CategoryRepository categoryRepository,
                         OperationRepository operationRepository) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public StringBuilder reportDiffForSelectedPeriod(int accountId, LocalDateTime from, LocalDateTime to) {
        if (to.isBefore(from)) {
            return new StringBuilder("Ошибка: Невозможно выделить период");
        }

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        List<Map.Entry<Integer, Operation>> accountOperations
                = operationRepository.getAccountOperations(accountId);

        for (Map.Entry<Integer, Operation> entry : accountOperations) {
            Operation operation = entry.getValue();
            LocalDateTime date = operation.getDate();

            if ((date.isAfter(from) || date.isEqual(from)) && (date.isBefore(to) || date.isEqual(to))) {
                BigDecimal amount = operation.getAmount();

                if (operation.getType() == OperationType.INCOME) {
                    totalIncome = totalIncome.add(amount);
                } else {
                    totalExpense = totalExpense.add(amount);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("ОТЧЕТ О ДОХОДАХ И РАСХОДАХ ЗА ПЕРИОД С ").append(from)
                .append(" ПО ").append(to).append('\n');
        result.append("Общий доход: ").append(totalIncome).append('\n');
        result.append("Общие траты: ").append(totalExpense).append('\n');
        result.append("Разница: ").append(totalIncome.subtract(totalExpense));

        return result;
    }

    public StringBuilder reportGroupedCategories(int accountId, OperationType type) {
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        List<Map.Entry<Integer, Operation>> accountOperations
                = operationRepository.getAccountOperations(accountId);

        for (Map.Entry<Integer, Operation> entry : accountOperations) {
            Operation operation = entry.getValue();

            if (operation.getType() == type) {
                Category category = categoryRepository.getCategory(operation.getCategoryId());
                String categoryName = category.getName();
                BigDecimal currentTotal = categoryTotals.getOrDefault(categoryName, BigDecimal.ZERO);
                categoryTotals.put(categoryName, currentTotal.add(operation.getAmount()));
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("ОТЧЕТ О ").append(type.toString().toUpperCase())
                .append("АХ ПО КАТЕГОРИЯМ").append('\n');

        if (categoryTotals.isEmpty()) {
            return new StringBuilder("Данные отсутствуют");
        }

        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            result.append("Категория [").append(entry.getKey()).append("]: ")
                    .append(entry.getValue()).append('\n');
        }

        return result;
    }
}

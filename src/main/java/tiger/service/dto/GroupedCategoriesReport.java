package tiger.service.dto;

import java.math.BigDecimal;
import java.util.Map;
import tiger.model.OperationType;

public record GroupedCategoriesReport(Map<String, BigDecimal> categoryTotals, OperationType type) {}

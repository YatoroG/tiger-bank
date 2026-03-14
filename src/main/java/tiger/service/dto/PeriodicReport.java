package tiger.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PeriodicReport(LocalDateTime from, LocalDateTime to,
                             BigDecimal totalIncome, BigDecimal totalExpense,
                             BigDecimal difference) {
}

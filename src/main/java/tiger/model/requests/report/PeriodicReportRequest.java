package tiger.model.requests.report;

import java.time.LocalDateTime;

public record PeriodicReportRequest(int accountId, LocalDateTime from, LocalDateTime to) {
}

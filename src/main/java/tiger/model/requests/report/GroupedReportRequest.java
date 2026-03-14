package tiger.model.requests.report;

import tiger.model.OperationType;

public record GroupedReportRequest(int accountId, OperationType type) {
}

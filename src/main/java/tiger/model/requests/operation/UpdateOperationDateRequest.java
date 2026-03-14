package tiger.model.requests.operation;

import java.time.LocalDateTime;

public record UpdateOperationDateRequest(int id, LocalDateTime newDate) {
}

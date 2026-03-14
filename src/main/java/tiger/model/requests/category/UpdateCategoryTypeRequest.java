package tiger.model.requests.category;

import tiger.model.OperationType;

public record UpdateCategoryTypeRequest(int id, OperationType newType) {
}

package tiger.model.requests.category;

import tiger.model.OperationType;

public record CreateCategoryRequest(String name, OperationType type) {
}

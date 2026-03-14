package tiger.service.factory;

import tiger.model.Category;
import tiger.model.OperationType;

public interface ICategoryFactory {
    Category createCategory(String name, OperationType type);
    void checkNextId(int maxCatId);
}

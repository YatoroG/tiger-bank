package tiger.service.factory.impl;

import org.springframework.stereotype.Component;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.service.factory.ICategoryFactory;

@Component
public class CategoryFactoryImpl implements ICategoryFactory {
    private int nextCategoryId = 1;

    @Override
    public Category createCategory(String name, OperationType type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: Название категории не может быть пустым");
        }

        if (type == null) {
            throw new IllegalArgumentException("Ошибка: Тип операции не может быть пустым");
        }

        return new Category(nextCategoryId++, name, type);
    }

    @Override
    public void checkNextId(int maxCatId) {
        this.nextCategoryId = maxCatId + 1;
    }
}

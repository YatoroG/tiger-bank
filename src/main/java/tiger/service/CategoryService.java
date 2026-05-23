package tiger.service;

import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private int nextCategoryId = 1;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public void createCategory(String name, OperationType type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: Название категории не может быть пустым");
        }

        if (type == null) {
            throw new IllegalArgumentException("Ошибка: Тип операции не может быть пустым");
        }

        Category category = new Category(nextCategoryId++, name, type);
        repository.add(category);
    }

    public void updateCategoryName(int id, String newName) {
        Category category = getCategory(id);
        category.setName(newName);
        repository.update(category);
    }

    public void updateCategoryType(int id, OperationType newType) {
        Category category = getCategory(id);
        category.setCategoryType(newType);
        repository.update(category);
    }

    public void deleteCategory(int id) {
        if (!repository.hasCategory(id)) {
            throw new IllegalArgumentException("Ошибка: Категория с " + id + " не найдена");
        }
        repository.delete(id);
    }

    public Category getCategory(int id) {
        if (!repository.hasCategory(id)) {
            throw new IllegalArgumentException("Ошибка: Категория с " + id + " не найдена");
        }
        return repository.getCategory(id);
    }

    public Collection<Category> getAllCategories() {
        return repository.getAllCategories().values();
    }

    public void checkNextId(int maxCatId) {
        this.nextCategoryId = maxCatId + 1;
    }
}

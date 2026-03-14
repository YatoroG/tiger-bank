package tiger.service;

import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.model.dto.CategoryFields;
import tiger.repository.CategoryRepository;
import tiger.service.factory.ICategoryFactory;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final ICategoryFactory factory;

    public CategoryService(CategoryRepository repository, ICategoryFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public void createCategory(String name, OperationType type) {
        Category category = factory.createCategory(name, type);
        repository.add(category);
    }

    public void updateCategoryName(int id, String newName) {
        Category category = searchCategory(id);
        category.setName(newName);
        repository.update(category);
    }

    public void updateCategoryType(int id, OperationType newType) {
        Category category = searchCategory(id);
        category.setCategoryType(newType);
        repository.update(category);
    }

    public void deleteCategory(int id) {
        if (!repository.hasCategory(id)) {
            throw new IllegalArgumentException("Ошибка: Категория с " + id + " не найдена");
        }
        repository.delete(id);
    }

    public CategoryFields getCategory(int id) {
        Category category = searchCategory(id);
        return category.splitCategory();
    }

    public Collection<CategoryFields> getAllCategories() {
        return repository.getAllCategories().values().stream()
                .map(Category::splitCategory).toList();
    }

    public void checkNextId(int maxCatId) {
        factory.checkNextId(maxCatId);
    }

    private Category searchCategory(int id) {
        if (!repository.hasCategory(id)) {
            throw new IllegalArgumentException("Ошибка: Категория с " + id + " не найдена");
        }
        return repository.getCategory(id);
    }
}

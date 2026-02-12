package tiger.service;

import java.util.Collection;
import org.springframework.stereotype.Service;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(String name, OperationType type) {
        Category category = new Category(name, type);
        repository.add(category);
        return category;
    }

    public void updateCategoryName(int id, String newName) {
        Category category = repository.getCategory(id);
        if (category != null) {
            category.setName(newName);
            repository.update(category);
        } else {
            System.out.println("Ошибка: Категория с " + id + " не найдена");
        }
    }

    public void updateCategoryType(int id, OperationType newType) {
        Category category = repository.getCategory(id);
        if (category != null) {
            category.setType(newType);
            repository.update(category);
        } else {
            System.out.println("Ошибка: Категория с " + id + " не найдена");
        }
    }

    public void deleteCategory(int id) {
        if (repository.hasCategory(id)) {
            repository.delete(id);
        } else {
            System.out.println("Ошибка: Категория с " + id + " не найдена");
        }
    }

    public boolean getCategory(int id) {
        return repository.hasCategory(id);
    }

    public Collection<Category> getAllCategories() {
        return repository.getAllCategories().values();
    }
}

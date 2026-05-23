package tiger.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import tiger.model.Category;
import tiger.model.OperationType;

@Repository
public class CategoryRepository {
    private final Map<Integer, Category> categories = new HashMap<>();

    public void add(Category category) {
        categories.put(category.getCategoryId(), category);
    }

    public void update(Category category) {
        categories.put(category.getCategoryId(), category);
    }

    public void delete(int id) {
        categories.remove(id);
    }

    public boolean hasCategory(int id) {
        return categories.containsKey(id);
    }

    public Category getCategory(int id) {
        return categories.get(id);
    }

    public Map<Integer, Category> getAllCategories() {
        return categories;
    }

    public OperationType getType(int id) {
        return categories.get(id).getCategoryType();
    }
}

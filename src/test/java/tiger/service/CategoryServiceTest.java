package tiger.service;

import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.Category;
import tiger.model.OperationType;
import tiger.repository.CategoryRepository;


import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = new CategoryRepository();
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testCreateAndGetAllCategories() {
        categoryService.createCategory("Зарплата", OperationType.INCOME);
        categoryService.createCategory("Еда", OperationType.EXPENSE);
        Collection<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
        assertNotNull(categoryService.getCategory(1));
        assertNotNull(categoryService.getCategory(2));
    }

    @Test
    void testUpdateCategoryName() {
        categoryService.createCategory("Старое имя", OperationType.INCOME);
        categoryService.updateCategoryName(1, "Новое имя");
        Category category = categoryRepository.getCategory(1);
        assertEquals("Новое имя", category.getName());
    }

    @Test
    void testUpdateCategoryType() {
        categoryService.createCategory("Смена категории", OperationType.INCOME);
        categoryService.updateCategoryType(1, OperationType.EXPENSE);
        Category category = categoryRepository.getCategory(1);
        assertEquals(OperationType.EXPENSE, category.getCategoryType());
    }

    @Test
    void testDeleteCategory() {
        categoryService.createCategory("Удаление категории", OperationType.EXPENSE);
        assertNotNull(categoryService.getCategory(1));
        categoryService.deleteCategory(1);
        assertNull(categoryService.getCategory(1));
        assertEquals(0, categoryService.getAllCategories().size());
    }

    @Test
    void testCheckNextId() {
        categoryService.checkNextId(10);
        categoryService.createCategory("Зарплата", OperationType.INCOME);
        assertNotNull(categoryService.getCategory(11));
    }
}

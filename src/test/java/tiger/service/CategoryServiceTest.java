package tiger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.OperationType;
import tiger.repository.CategoryRepository;
import tiger.service.factory.impl.CategoryFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    private CategoryService service;

    @BeforeEach
    void setUp() {
        CategoryRepository repository = new CategoryRepository();
        CategoryFactoryImpl factory = new CategoryFactoryImpl();
        service = new CategoryService(repository, factory);
    }

    @Test
    void testCreateAndGetAllCategories() {
        service.createCategory("Зарплата", OperationType.INCOME);
        service.createCategory("Еда", OperationType.EXPENSE);

        var categories = service.getAllCategories();
        assertEquals(2, categories.size());

        var incomeCat = categories.stream()
                .filter(c -> c.type() == OperationType.INCOME)
                .findFirst()
                .orElseThrow();
        assertEquals(1, incomeCat.id());
        assertEquals("Зарплата", incomeCat.name());

        var expenseCat = categories.stream()
                .filter(c -> c.type() == OperationType.EXPENSE)
                .findFirst()
                .orElseThrow();
        assertEquals(2, expenseCat.id());
        assertEquals("Еда", expenseCat.name());
    }

    @Test
    void testUpdateCategoryName() {
        service.createCategory("Старое имя", OperationType.INCOME);
        service.updateCategoryName(1, "Новое имя");
        var category = service.getCategory(1);
        assertEquals("Новое имя", category.name());
    }

    @Test
    void testUpdateCategoryType() {
        service.createCategory("Смена категории", OperationType.INCOME);
        service.updateCategoryType(1, OperationType.EXPENSE);
        var category = service.getCategory(1);
        assertEquals(OperationType.EXPENSE, category.type());
    }

    @Test
    void testDeleteCategory() {
        service.createCategory("Удаление категории", OperationType.EXPENSE);
        assertNotNull(service.getCategory(1));
        service.deleteCategory(1);
        assertThrows(IllegalArgumentException.class, () -> service.getCategory(1));
        assertEquals(0, service.getAllCategories().size());
    }

    @Test
    void testGetCategory() {
        service.createCategory("Получение категории", OperationType.INCOME);
        assertNotNull(service.getCategory(1));
    }

    @Test
    void testCheckNextId() {
        service.checkNextId(10);
        service.createCategory("Зарплата", OperationType.INCOME);
        assertNotNull(service.getCategory(11));
    }

    @Test
    void testInvalidDeleteIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.getCategory(1));
        assertThrows(IllegalArgumentException.class, () -> service.deleteCategory(1));
    }
}

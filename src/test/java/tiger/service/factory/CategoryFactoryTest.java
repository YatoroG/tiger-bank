package tiger.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.OperationType;
import tiger.service.factory.impl.CategoryFactoryImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryFactoryTest {
    private CategoryFactoryImpl factory;

    @BeforeEach
    void setUp() {
        factory = new CategoryFactoryImpl();
    }

    @Test
    void testCreateCategoryWithNullNameThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createCategory(null, OperationType.EXPENSE)
        );
        assertEquals("Ошибка: Название категории не может быть пустым", exception.getMessage());
    }

    @Test
    void testCreateCategoryWithEmptyNameThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createCategory("   ", OperationType.EXPENSE)
        );
        assertEquals("Ошибка: Название категории не может быть пустым", exception.getMessage());
    }

    @Test
    void testCreateCategoryWithNullTypeThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createCategory("Еда", null)
        );
        assertEquals("Ошибка: Тип операции не может быть пустым", exception.getMessage());
    }
}

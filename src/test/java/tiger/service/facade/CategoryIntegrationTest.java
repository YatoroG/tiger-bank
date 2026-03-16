package tiger.service.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiger.model.OperationType;
import tiger.model.requests.category.*;
import tiger.repository.CategoryRepository;
import tiger.service.CategoryService;
import tiger.service.command.CommandExecutor;
import tiger.service.command.category.CreateCategoryCommand;
import tiger.service.command.category.DeleteCategoryCommand;
import tiger.service.command.category.UpdateCategoryNameCommand;
import tiger.service.command.category.UpdateCategoryTypeCommand;
import tiger.service.factory.impl.CategoryFactoryImpl;


import static org.junit.jupiter.api.Assertions.*;

class CategoryIntegrationTest {
    private CategoryFacade facade;
    private CommandExecutor executor;

    @BeforeEach
    void setUp() {
        CategoryRepository repository = new CategoryRepository();
        CategoryFactoryImpl factory = new CategoryFactoryImpl();
        CategoryService service = new CategoryService(repository, factory);

        this.facade = new CategoryFacade(service);
        this.executor = new CommandExecutor();
    }

    @Test
    void testCreateCategoryThroughCommand() {
        var request = new CreateCategoryRequest("Еда", OperationType.EXPENSE);
        var command = new CreateCategoryCommand(facade, request);
        executor.execute(command);
        var categories = facade.getAll();
        assertEquals(1, categories.size());
        assertEquals("Еда", categories.iterator().next().name());
    }

    @Test
    void testUpdateCategoryName() {
        facade.create(new CreateCategoryRequest("Еда", OperationType.EXPENSE));
        var categoryId = facade.getAll().iterator().next().id();

        var request = new UpdateCategoryNameRequest(categoryId, "Продукты");
        var command = new UpdateCategoryNameCommand(facade, request);
        executor.execute(command);

        var updated = facade.getCategory(new GetCategoryRequest(categoryId));
        assertEquals("Продукты", updated.name());
    }

    @Test
    void testUpdateCategoryType() {
        facade.create(new CreateCategoryRequest("Подарок", OperationType.INCOME));
        var categoryId = facade.getAll().iterator().next().id();

        var request = new UpdateCategoryTypeRequest(categoryId, OperationType.EXPENSE);
        var command = new UpdateCategoryTypeCommand(facade, request);
        executor.execute(command);

        var updated = facade.getCategory(new GetCategoryRequest(categoryId));
        assertEquals(OperationType.EXPENSE, updated.type());
    }

    @Test
    void testDeleteCategory() {
        facade.create(new CreateCategoryRequest("На удаление", OperationType.EXPENSE));
        var categoryId = facade.getAll().iterator().next().id();
        assertFalse(facade.getAll().isEmpty());

        var request = new DeleteCategoryRequest(categoryId);
        var command = new DeleteCategoryCommand(facade, request);
        executor.execute(command);

        assertTrue(facade.getAll().isEmpty());
    }

    @Test
    void testGetCategoryThrowsExceptionIfNotFound() {
        var request = new GetCategoryRequest(999);
        assertThrows(RuntimeException.class, () -> facade.getCategory(request));
    }
}

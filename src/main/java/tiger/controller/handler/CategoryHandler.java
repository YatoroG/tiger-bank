package tiger.controller.handler;

import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.model.OperationType;
import tiger.model.requests.category.*;
import tiger.service.command.CommandExecutor;
import tiger.service.command.category.*;
import tiger.service.facade.CategoryFacade;

@Component
public class CategoryHandler {
    private final CategoryFacade categoryFacade;
    private final CommandExecutor executor;
    private final InputParser input;

    public CategoryHandler(CategoryFacade categoryFacade, CommandExecutor executor, InputParser input) {
        this.categoryFacade = categoryFacade;
        this.executor = executor;
        this.input = input;
    }

    public void handleCategories() {
        System.out.println("1. Список категорий");
        System.out.println("2. Посмотреть категорию");
        System.out.println("3. Создать категорию");
        System.out.println("4. Удалить категорию");
        System.out.println("5. Обновить название категории");
        System.out.println("6. Обновить тип категории");
        int num = input.readInt("Введите пункт меню");

        switch (num) {
            case 1:
                categoryFacade.getAll().forEach(c ->
                        System.out.println(c.id() + ": [" +
                                c.type() + "] " +
                                c.name()));
                break;
            case 2: {
                int id = input.readInt("ID категории");

                var req = new GetCategoryRequest(id);
                var cmd = new GetCategoryCommand(categoryFacade, req);
                executor.execute(cmd);
                var category = cmd.getResult();
                System.out.println(category.id() + ": [" +
                        category.type() + "] " +
                        category.name());
                break;
            }
            case 3: {
                int type = input.readInt("Тип категории (1 - Доход, 2 - Расход)");
                String name = input.readString("Название категории");

                var cmd = new CreateCategoryRequest(name, OperationType.fromInt(type - 1));
                executor.execute(new CreateCategoryCommand(categoryFacade, cmd));
                break;
            }
            case 4: {
                int id = input.readInt("ID категории");

                var cmd = new DeleteCategoryRequest(id);
                executor.execute(new DeleteCategoryCommand(categoryFacade, cmd));
                break;
            }
            case 5: {
                int id = input.readInt("ID категории");
                String name = input.readString("Новое название категории");

                var cmd = new UpdateCategoryNameRequest(id, name);
                executor.execute(new UpdateCategoryNameCommand(categoryFacade, cmd));
                break;
            }
            case 6: {
                int id = input.readInt("ID категории");
                int type = input.readInt("Тип категории (1 - Доход, 2 - Расход)");

                var cmd = new UpdateCategoryTypeRequest(id, OperationType.fromInt(type - 1));
                executor.execute(new UpdateCategoryTypeCommand(categoryFacade, cmd));
                break;
            }
            default:
                System.out.print("Введите пункт меню");
        }
    }
}

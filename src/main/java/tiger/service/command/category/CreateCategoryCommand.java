package tiger.service.command.category;

import tiger.model.requests.category.CreateCategoryRequest;
import tiger.service.command.Command;
import tiger.service.facade.CategoryFacade;

public class CreateCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final CreateCategoryRequest request;

    public CreateCategoryCommand(CategoryFacade facade, CreateCategoryRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.create(request);
    }

    @Override
    public String getName() {
        return "Добавление категории";
    }
}

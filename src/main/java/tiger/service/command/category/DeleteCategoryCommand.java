package tiger.service.command.category;

import tiger.model.requests.category.DeleteCategoryRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.CategoryFacade;

public class DeleteCategoryCommand implements ICommand {
    private final CategoryFacade facade;
    private final DeleteCategoryRequest request;

    public DeleteCategoryCommand(CategoryFacade facade, DeleteCategoryRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.delete(request);
    }

    @Override
    public String getName() {
        return "Удаление категории";
    }
}

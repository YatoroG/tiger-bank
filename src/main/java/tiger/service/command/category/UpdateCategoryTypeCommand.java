package tiger.service.command.category;

import tiger.model.requests.category.UpdateCategoryTypeRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.CategoryFacade;

public class UpdateCategoryTypeCommand implements ICommand {
    private final CategoryFacade facade;
    private final UpdateCategoryTypeRequest request;

    public UpdateCategoryTypeCommand(CategoryFacade facade, UpdateCategoryTypeRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateType(request);
    }

    @Override
    public String getName() {
        return "Изменение типа категории";
    }
}

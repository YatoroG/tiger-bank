package tiger.service.command.category;

import tiger.model.requests.category.UpdateCategoryTypeRequest;
import tiger.service.command.Command;
import tiger.service.facade.CategoryFacade;

public class UpdateCategoryTypeCommand implements Command {
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

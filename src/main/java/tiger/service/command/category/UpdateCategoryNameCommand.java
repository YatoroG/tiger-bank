package tiger.service.command.category;

import tiger.model.requests.category.UpdateCategoryNameRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.CategoryFacade;

public class UpdateCategoryNameCommand implements ICommand {
    private final CategoryFacade facade;
    private final UpdateCategoryNameRequest request;

    public UpdateCategoryNameCommand(CategoryFacade facade, UpdateCategoryNameRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateName(request);
    }

    @Override
    public String getName() {
        return "Изменение названия категории";
    }
}

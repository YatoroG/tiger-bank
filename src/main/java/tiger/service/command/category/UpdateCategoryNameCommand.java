package tiger.service.command.category;

import tiger.model.requests.category.UpdateCategoryNameRequest;
import tiger.service.command.Command;
import tiger.service.facade.CategoryFacade;

public class UpdateCategoryNameCommand implements Command {
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

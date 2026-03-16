package tiger.service.command.category;

import tiger.model.dto.CategoryFields;
import tiger.model.requests.category.GetCategoryRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.CategoryFacade;

public class GetCategoryCommand implements ICommand {
    private final CategoryFacade facade;
    private final GetCategoryRequest request;
    private CategoryFields category;

    public GetCategoryCommand(CategoryFacade facade, GetCategoryRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        this.category = facade.getCategory(request);
    }

    @Override
    public String getName() {
        return "Просмотр категории";
    }

    public CategoryFields getResult() {
        return category;
    }
}

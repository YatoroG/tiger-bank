package tiger.service.command.operation;

import tiger.model.requests.operation.UpdateOperationCategoryRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.OperationFacade;

public class UpdateOperationCategoryCommand implements ICommand {
    private final OperationFacade facade;
    private final UpdateOperationCategoryRequest request;

    public UpdateOperationCategoryCommand(OperationFacade facade, UpdateOperationCategoryRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateCategory(request);
    }

    @Override
    public String getName() {
        return "Изменение категории операции";
    }
}

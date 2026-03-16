package tiger.service.command.operation;

import tiger.model.requests.operation.AddOperationRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.OperationFacade;

public class AddOperationCommand implements ICommand {
    private final OperationFacade facade;
    private final AddOperationRequest request;

    public AddOperationCommand(OperationFacade facade, AddOperationRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.create(request);
    }

    @Override
    public String getName() {
        return "Добавление операции";
    }
}

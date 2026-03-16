package tiger.service.command.operation;

import tiger.model.requests.operation.DeleteOperationRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.OperationFacade;

public class DeleteOperationCommand implements ICommand {
    private final OperationFacade facade;
    private final DeleteOperationRequest request;

    public DeleteOperationCommand(OperationFacade facade, DeleteOperationRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.delete(request);
    }

    @Override
    public String getName() {
        return "Удаление операции";
    }
}

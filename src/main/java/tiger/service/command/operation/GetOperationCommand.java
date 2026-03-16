package tiger.service.command.operation;

import tiger.model.dto.OperationFields;
import tiger.model.requests.operation.GetOperationRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.OperationFacade;

public class GetOperationCommand implements ICommand {
    private final OperationFacade facade;
    private final GetOperationRequest request;
    private OperationFields operation;

    public GetOperationCommand(OperationFacade facade, GetOperationRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.getOperation(request);
    }

    @Override
    public String getName() {
        return "Просмотр операции";
    }

    public OperationFields getResult() {
        return operation;
    }
}

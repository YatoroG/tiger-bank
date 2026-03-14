package tiger.service.command.operation;

import tiger.model.requests.operation.UpdateOperationDateRequest;
import tiger.service.command.Command;
import tiger.service.facade.OperationFacade;

public class UpdateOperationDateCommand implements Command {
    private final OperationFacade facade;
    private final UpdateOperationDateRequest request;

    public UpdateOperationDateCommand(OperationFacade facade, UpdateOperationDateRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateDate(request);
    }

    @Override
    public String getName() {
        return "Изменение даты операции";
    }
}

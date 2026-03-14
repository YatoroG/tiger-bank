package tiger.service.command.operation;

import tiger.model.requests.operation.UpdateOperationAmountRequest;
import tiger.service.command.Command;
import tiger.service.facade.OperationFacade;

public class UpdateOperationAmountCommand implements Command {
    private final OperationFacade facade;
    private final UpdateOperationAmountRequest request;

    public UpdateOperationAmountCommand(OperationFacade facade, UpdateOperationAmountRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateAmount(request);
    }

    @Override
    public String getName() {
        return "Изменение суммы операции";
    }
}

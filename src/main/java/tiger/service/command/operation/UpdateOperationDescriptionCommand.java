package tiger.service.command.operation;

import tiger.model.requests.operation.UpdateOperationDescriptionRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.OperationFacade;

public class UpdateOperationDescriptionCommand implements ICommand {
    private final OperationFacade facade;
    private final UpdateOperationDescriptionRequest request;

    public UpdateOperationDescriptionCommand(OperationFacade facade,
                                             UpdateOperationDescriptionRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateDescription(request);
    }

    @Override
    public String getName() {
        return "Изменение описания операции";
    }
}

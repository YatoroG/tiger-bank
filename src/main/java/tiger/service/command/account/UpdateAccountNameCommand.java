package tiger.service.command.account;

import tiger.model.requests.account.UpdateAccountNameRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.BankAccountFacade;

public class UpdateAccountNameCommand implements ICommand {
    private final BankAccountFacade facade;
    private final UpdateAccountNameRequest request;

    public UpdateAccountNameCommand(BankAccountFacade facade, UpdateAccountNameRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.updateName(request);
    }

    @Override
    public String getName() {
        return "Изменение наименования счета";
    }
}

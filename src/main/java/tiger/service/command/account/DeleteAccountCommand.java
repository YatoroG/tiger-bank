package tiger.service.command.account;

import tiger.model.requests.account.DeleteAccountRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.BankAccountFacade;

public class DeleteAccountCommand implements ICommand {
    private final BankAccountFacade facade;
    private final DeleteAccountRequest request;

    public DeleteAccountCommand(BankAccountFacade facade, DeleteAccountRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.delete(request);
    }

    @Override
    public String getName() {
        return "Удаление счета";
    }
}

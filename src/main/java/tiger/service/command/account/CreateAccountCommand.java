package tiger.service.command.account;

import tiger.model.requests.account.CreateAccountRequest;
import tiger.service.command.ICommand;
import tiger.service.facade.BankAccountFacade;

public class CreateAccountCommand implements ICommand {
    private final BankAccountFacade facade;
    private final CreateAccountRequest request;

    public CreateAccountCommand(BankAccountFacade facade, CreateAccountRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        facade.create(request);
    }

    @Override
    public String getName() {
        return "Добавление счета";
    }
}

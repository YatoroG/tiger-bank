package tiger.service.command.account;

import tiger.model.requests.account.CreateAccountRequest;
import tiger.service.command.Command;
import tiger.service.facade.BankAccountFacade;

public class CreateAccountCommand implements Command {
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

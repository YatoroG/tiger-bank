package tiger.service.command.account;

import tiger.model.dto.BankAccountFields;
import tiger.model.requests.account.GetAccountRequest;
import tiger.service.command.Command;
import tiger.service.facade.BankAccountFacade;

public class GetAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final GetAccountRequest request;
    private BankAccountFields account;

    public GetAccountCommand(BankAccountFacade facade, GetAccountRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        this.account = facade.getAccount(request);
    }

    @Override
    public String getName() {
        return "Просмотр счета";
    }

    public BankAccountFields getResult() {
        return account;
    }
}

package tiger.controller.handler;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.model.requests.account.*;
import tiger.service.facade.BankAccountFacade;

@Component
public class BankAccountHandler {
    private final BankAccountFacade accountFacade;
    private final InputParser input;

    public BankAccountHandler(BankAccountFacade accountFacade, InputParser input) {
        this.accountFacade = accountFacade;
        this.input = input;
    }

    public void handleAccounts() {
        System.out.println("1. Список счетов");
        System.out.println("2. Посмотреть счет");
        System.out.println("3. Создать счет");
        System.out.println("4. Удалить счет");
        System.out.println("5. Обновить название счета");
        int num = input.readInt("Введите пункт меню");

        switch (num) {
            case 1: {
                accountFacade.getAll().forEach(a ->
                        System.out.println(a.id() + ": [" +
                                a.name() + "] " +
                                a.balance()));
                break;
            }
            case 2: {
                int id = input.readInt("ID счета");

                var cmd = new GetAccountRequest(id);
                var account = accountFacade.getAccount(cmd);
                System.out.println(account.id() + ": [" +
                        account.name() + "] " +
                        account.balance());
                break;
            }
            case 3: {
                String name = input.readString("Наименование");
                BigDecimal balance = input.readBigDecimal("Начальный баланс");

                var cmd = new CreateAccountRequest(name, balance);
                accountFacade.create(cmd);
                System.out.println("Счет добавлен");
                break;
            }
            case 4: {
                int id = input.readInt("ID счета");

                var cmd = new DeleteAccountRequest(id);
                accountFacade.delete(cmd);
                System.out.println("Счет удален");
                break;
            }
            case 5: {
                int id = input.readInt("ID счета");
                String name = input.readString("Новое наименование");

                var cmd = new UpdateAccountNameRequest(id, name);
                accountFacade.updateName(cmd);
                System.out.println("Наименование счета обновлено");
                break;
            }
            default:
                System.out.print("Введите пункт меню");
        }
    }
}

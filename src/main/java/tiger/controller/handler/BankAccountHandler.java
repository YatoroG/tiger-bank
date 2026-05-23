package tiger.controller.handler;

import java.math.BigDecimal;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.service.BankAccountService;

@Component
public class BankAccountHandler {
    private final BankAccountService bankAccountService;
    private final Scanner scanner = new Scanner(System.in);

    public BankAccountHandler(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    public void handleAccounts() {
        System.out.println("1. Создать счет");
        System.out.println("2. Список счетов");
        int num = Integer.parseInt(scanner.nextLine());

        if (num == 1) {
            System.out.print("Наименование: ");
            String name = scanner.nextLine();
            System.out.print("Начальный баланс: ");
            BigDecimal balance = new BigDecimal(scanner.nextLine());

            bankAccountService.createAccount(name, balance);
            System.out.println("Счет добавлен");
        } else if (num == 2) {
            bankAccountService.getAllAccounts().forEach(a ->
                    System.out.println(a.getAccountId() + ": " + a.getName() + " " + a.getBalance()));
        }
    }
}

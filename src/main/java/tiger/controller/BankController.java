package tiger.controller;

import java.util.Scanner;
import org.springframework.stereotype.Controller;
import tiger.controller.handler.*;

@Controller
public class BankController {
    private final BankAccountHandler bankAccountHandler;
    private final CategoryHandler categoryHandler;
    private final OperationHandler operationHandler;
    private final ReportHandler reportHandler;
    private final ExchangeHandler exchangeHandler;

    private final Scanner scanner = new Scanner(System.in);

    public BankController(BankAccountHandler bankAccountHandler, CategoryHandler categoryHandler,
                          OperationHandler operationHandler, ReportHandler reportHandler,
                          ExchangeHandler exchangeHandler) {
        this.bankAccountHandler = bankAccountHandler;
        this.categoryHandler = categoryHandler;
        this.operationHandler = operationHandler;
        this.reportHandler = reportHandler;
        this.exchangeHandler = exchangeHandler;
    }

    public void start() {
        System.out.print(tiger);
        while (true) {
            System.out.println();
            System.out.println("--- ТИГРБАНК. УЧЕТ ФИНАНСОВ ---");
            System.out.println("1. Счета (Создать/Список)");
            System.out.println("2. Категории (Создать/Список)");
            System.out.println("3. Операции (Создать/Список)");
            System.out.println("4. Аналитика (Все операции/По категориям)");
            System.out.println("5. Данные (Импорт/Экспорт)");
            System.out.println("0. Выход");

            int num = Integer.parseInt(scanner.nextLine());
            if (num == 0) break;

            switch (num) {
                case 1 -> bankAccountHandler.handleAccounts();
                case 2 -> categoryHandler.handleCategories();
                case 3 -> operationHandler.handleOperations();
                case 4 -> reportHandler.handleReports();
                case 5 -> exchangeHandler.handleExchange();
                default -> System.out.println("Введите пункт меню");
            }
        }
    }

    private final String tiger = """
                 (^\\-==-/^)
                 >\\\\ == //<
                :== q''p ==:     _
                 .__ qp __.    .' )                 Вас приветствует ТигрБанк!
                  / ^--^ \\    /\\.'            Добро пожаловать в модуль учета финансов
                 /_`    / )  '\\/
                 (  )  \\  |-'-/
                 \\^^,   |-|--'
                ( `'    |_| )
                 \\-     |-|/
                (( )^---( ))
            """;
}

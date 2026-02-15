package tiger.controller.handler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.model.OperationType;
import tiger.service.IOperationService;

@Component
public class OperationHandler {
    private final IOperationService operationService;
    private final Scanner scanner = new Scanner(System.in);

    public OperationHandler(IOperationService operationService) {
        this.operationService = operationService;
    }

    public void handleOperations() {
        System.out.println("1. Создать операцию");
        System.out.println("2. Список последних пяти операций");
        int num = Integer.parseInt(scanner.nextLine());

        if (num == 1) {
            System.out.print("ID счета: ");
            int accId = Integer.parseInt(scanner.nextLine());
            System.out.print("Сумма: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            System.out.print("Тип операции (1 - Доход, 2 - Расход): ");
            OperationType type = scanner.nextLine().equals("1")
                    ? OperationType.INCOME : OperationType.EXPENSE;
            System.out.print("ID категории: ");
            int catId = Integer.parseInt(scanner.nextLine());
            System.out.print("Описание: ");
            String desc = scanner.nextLine();

            operationService.addOperation(type, accId, amount, LocalDateTime.now(), desc, catId);
            System.out.println("Операция добавлена");
        } else if (num == 2) {
            operationService.getLastFiveOperations().forEach(o ->
                    System.out.println(o.getOperationId() + ": Счет №" + o.getBankAccountId() + ", " +
                            o.getAmount() + " [" + o.getType() + "] " + o.getCategoryId() +
                            ", " + o.getDescription()));
        }
    }
}

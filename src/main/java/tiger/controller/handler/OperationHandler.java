package tiger.controller.handler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.model.OperationType;
import tiger.model.requests.operation.*;
import tiger.service.command.CommandExecutor;
import tiger.service.command.operation.*;
import tiger.service.facade.OperationFacade;

@Component
public class OperationHandler {
    private final OperationFacade operationFacade;
    private final CommandExecutor executor;
    private final InputParser input;

    public OperationHandler(OperationFacade operationFacade, CommandExecutor executor, InputParser input) {
        this.operationFacade = operationFacade;
        this.executor = executor;
        this.input = input;
    }

    public void handleOperations() {
        System.out.println("1. Список всех операций");
        System.out.println("2. Список последних пяти операций");
        System.out.println("3. Просмотреть операцию");
        System.out.println("4. Создать операцию");
        System.out.println("5. Удалить операцию");
        System.out.println("6. Обновить сумму операции");
        System.out.println("7. Обновить категорию операции");
        System.out.println("8. Обновить дату операции");
        System.out.println("9. Обновить описание операции");
        int num = input.readInt("Введите пункт меню");

        switch (num) {
            case 1: {
                operationFacade.getAll().forEach(o ->
                        System.out.println(o.id() + ": Счет №" + o.bankAccountId() + ", " +
                                o.amount() + " [" + o.type() + "] " + o.categoryId() +
                                ", " + o.description()));
                break;
            }
            case 2: {
                operationFacade.getLastFive().forEach(o ->
                        System.out.println(o.id() + ": Счет №" + o.bankAccountId() + ", " +
                                o.amount() + " [" + o.type() + "] " + o.categoryId() +
                                ", " + o.description()));
                break;
            }
            case 3: {
                int id = input.readInt("ID операции");

                var req = new GetOperationRequest(id);
                var cmd = new GetOperationCommand(operationFacade, req);
                executor.execute(cmd);
                var operation = cmd.getResult();
                System.out.println(operation.id() + ": Счет №" + operation.bankAccountId() + ", " +
                        operation.amount() + " [" + operation.type() + "] " + operation.categoryId() +
                        ", " + operation.description());
                break;
            }
            case 4: {
                int accId = input.readInt("ID счета");
                BigDecimal amount = input.readBigDecimal("Сумма");
                int type = input.readInt("Тип (1 - Доход, 2 - Расход)");
                int catId = input.readInt("ID категории");
                String desc = input.readString("Описание");

                var cmd = new AddOperationRequest(
                        type == 1 ? OperationType.INCOME : OperationType.EXPENSE,
                        accId, amount, LocalDateTime.now(), desc, catId
                );
                executor.execute(new AddOperationCommand(operationFacade, cmd));
                System.out.println("Операция добавлена");
                break;
            }
            case 5: {
                int id = input.readInt("ID операции");

                var cmd = new DeleteOperationRequest(id);
                executor.execute(new DeleteOperationCommand(operationFacade, cmd));
                System.out.println("Операция удалена");
                break;
            }
            case 6: {
                int id = input.readInt("ID операции");
                BigDecimal newAmount = input.readBigDecimal("Новая сумма операции");

                var cmd = new UpdateOperationAmountRequest(id, newAmount);
                executor.execute(new UpdateOperationAmountCommand(operationFacade, cmd));
                System.out.println("Сумма операции обновлена");
                break;
            }
            case 7: {
                int id = input.readInt("ID операции");
                int newCat = input.readInt("Новая категория операции");

                var cmd = new UpdateOperationCategoryRequest(id, newCat);
                executor.execute(new UpdateOperationCategoryCommand(operationFacade, cmd));
                System.out.println("Категория операции обновлена");
                break;
            }
            case 8: {
                int id = input.readInt("ID операции");
                LocalDateTime newDate = input.readDate("Новая дата операции");

                var cmd = new UpdateOperationDateRequest(id, newDate);
                executor.execute(new UpdateOperationDateCommand(operationFacade, cmd));
                System.out.println("Дата операции обновлена");
                break;
            }
            case 9: {
                int id = input.readInt("ID операции");
                String newDescription = input.readString("Новое описание операции");

                var cmd = new UpdateOperationDescriptionRequest(id, newDescription);
                executor.execute(new UpdateOperationDescriptionCommand(operationFacade, cmd));
                System.out.println("Описание операции обновлено");
                break;
            }
            default:
                System.out.print("Введите пункт меню");
        }
    }
}

package tiger.controller.handler;

import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.service.command.CommandExecutor;
import tiger.service.command.exchange.ExportCommand;
import tiger.service.command.exchange.ImportCommand;
import tiger.service.file.exchange.ExchangeService;

@Component
public class ExchangeHandler {
    private final ExchangeService exchangeService;
    private final CommandExecutor executor;
    private final InputParser input;

    public ExchangeHandler(ExchangeService exchangeService, CommandExecutor executor, InputParser input) {
        this.exchangeService = exchangeService;
        this.executor = executor;
        this.input = input;
    }

    public void handleExchange() {
        System.out.println("1. Экспорт в CSV");
        System.out.println("2. Импорт из CSV");
        System.out.println("3. Экспорт в JSON");
        System.out.println("4. Импорт из JSON");
        System.out.println("5. Экспорт в YAML");
        System.out.println("6. Импорт из YAML");
        int num = input.readInt("Введите пункт меню");

        switch (num) {
            case 1: {
                var cmd = new ExportCommand(exchangeService, "csv");
                executor.execute(cmd);
                break;
            }
            case 2: {
                var cmd = new ImportCommand(exchangeService, "csv");
                executor.execute(cmd);
                break;
            }
            case 3: {
                var cmd = new ExportCommand(exchangeService, "json");
                executor.execute(cmd);
                break;
            }
            case 4: {
                var cmd = new ImportCommand(exchangeService, "json");
                executor.execute(cmd);
                break;
            }
            case 5: {
                var cmd = new ExportCommand(exchangeService, "yaml");
                executor.execute(cmd);
                break;
            }
            case 6: {
                var cmd = new ImportCommand(exchangeService, "yaml");
                executor.execute(cmd);
                break;
            }
            default:
                System.out.println("Введите пункт меню");
        }
    }
}

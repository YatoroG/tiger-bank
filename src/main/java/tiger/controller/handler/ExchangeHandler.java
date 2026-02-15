package tiger.controller.handler;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.service.file.exchange.ExchangeService;

@Component
public class ExchangeHandler {
    private final ExchangeService exchangeService;
    private final Scanner scanner = new Scanner(System.in);

    public ExchangeHandler(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public void handleExchange() {
        System.out.println("1. Экспорт в CSV");
        System.out.println("2. Импорт из CSV");
        System.out.println("3. Экспорт в JSON");
        System.out.println("4. Импорт из JSON");
        System.out.println("5. Экспорт в YAML");
        System.out.println("6. Импорт из YAML");
        int num = Integer.parseInt(scanner.nextLine());

        if (num == 1) {
            exchangeService.performCsvExport();
            System.out.println("Данные успешно сохранены в файлы *.csv");
        } else if (num == 2) {
            try {
                exchangeService.performCsvImport();
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта CSV: " + e.getMessage());
            }
        } else if (num == 3) {
            exchangeService.performJsonExport();
            System.out.println("Данные успешно сохранены в файлы *.json");
        } else if (num == 4) {
            try {
                exchangeService.performJsonImport();
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта JSON: " + e.getMessage());
            }
        } else if (num == 5) {
            exchangeService.performYamlExport();
            System.out.println("Данные успешно сохранены в файлы *.yml");
        } else if (num == 6) {
            try {
                exchangeService.performYamlImport();
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта YAML: " + e.getMessage());
            }
        }
    }
}

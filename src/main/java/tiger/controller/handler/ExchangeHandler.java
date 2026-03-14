package tiger.controller.handler;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.service.file.exchange.ExchangeService;

@Component
public class ExchangeHandler {
    private final ExchangeService exchangeService;
    private final InputParser input;

    public ExchangeHandler(ExchangeService exchangeService, InputParser input) {
        this.exchangeService = exchangeService;
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

        if (num == 1) {
            exchangeService.performExport("csv");
            System.out.println("Данные успешно сохранены в файлы *.csv");
        } else if (num == 2) {
            try {
                exchangeService.performImport("csv");
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта CSV: " + e.getMessage());
            }
        } else if (num == 3) {
            exchangeService.performExport("json");
            System.out.println("Данные успешно сохранены в файлы *.json");
        } else if (num == 4) {
            try {
                exchangeService.performImport("json");
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта JSON: " + e.getMessage());
            }
        } else if (num == 5) {
            exchangeService.performExport("yaml");
            System.out.println("Данные успешно сохранены в файлы *.yml");
        } else if (num == 6) {
            try {
                exchangeService.performImport("yaml");
                System.out.println("Данные успешно восстановлены");
            } catch (Exception e) {
                System.err.println("Ошибка импорта YAML: " + e.getMessage());
            }
        }
    }
}

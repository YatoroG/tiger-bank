package tiger.controller.handler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.model.OperationType;
import tiger.service.ReportService;
import tiger.service.dto.GroupedCategoriesReport;
import tiger.service.dto.PeriodicReport;

@Component
public class ReportHandler {
    private final ReportService reportService;
    private final Scanner scanner = new Scanner(System.in);

    public ReportHandler(ReportService reportService) {
        this.reportService = reportService;
    }

    public void handleReports() {
        System.out.println("1. Вывести отчет о доходах и расходах счета за период");
        System.out.println("2. Вывести отчет о доходах/расходах счета по категориям");
        int num = Integer.parseInt(scanner.nextLine());

        if (num == 1) {
            System.out.print("Номер счета: ");
            int accId = Integer.parseInt(scanner.nextLine());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            System.out.print("Период с (ДД.ММ.ГГГГ): ");
            LocalDateTime from = LocalDate.parse(scanner.nextLine(), formatter).atStartOfDay();
            System.out.print("Период до (ДД.ММ.ГГГГ): ");
            LocalDateTime to = LocalDate.parse(scanner.nextLine(), formatter).atStartOfDay();

            PeriodicReport pr = reportService.reportDiffForSelectedPeriod(accId, from, to);
            System.out.println("ОТЧЕТ О ДОХОДАХ И РАСХОДАХ ЗА ПЕРИОД С " + pr.from() + " ПО " + pr.to());
            System.out.println("Общий доход: " + pr.totalIncome());
            System.out.println("Общие траты: " + pr.totalExpense());
            System.out.println("Разница: " + pr.difference());
        } else if (num == 2) {
            System.out.print("Номер счета: ");
            int accId = Integer.parseInt(scanner.nextLine());
            System.out.print("Тип категории (1 - Доход, 2 - Расход): ");
            int typeId = Integer.parseInt(scanner.nextLine()) - 1;

            GroupedCategoriesReport gcr = reportService.reportGroupedCategories(accId,
                    OperationType.fromInt(typeId));
            System.out.println("ОТЧЕТ О " + gcr.type().toString().toUpperCase() + "АХ ПО КАТЕГОРИЯМ");

            if (gcr.categoryTotals().isEmpty()) {
                System.out.println("Данные отсутствуют");
            }

            for (Map.Entry<String, BigDecimal> entry : gcr.categoryTotals().entrySet()) {
                System.out.println("Категория: " + entry.getKey() + " [" + entry.getValue() + "]");
            }
        }
    }
}

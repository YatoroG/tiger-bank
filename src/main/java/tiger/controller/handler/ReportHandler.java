package tiger.controller.handler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Component;
import tiger.controller.utils.InputParser;
import tiger.model.OperationType;
import tiger.service.ReportService;
import tiger.service.dto.GroupedCategoriesReport;
import tiger.service.dto.PeriodicReport;

@Component
public class ReportHandler {
    private final ReportService reportService;
    private final InputParser input;

    public ReportHandler(ReportService reportService, InputParser input) {
        this.reportService = reportService;
        this.input = input;
    }

    public void handleReports() {
        System.out.println("1. Вывести отчет о доходах и расходах счета за период");
        System.out.println("2. Вывести отчет о доходах/расходах счета по категориям");
        int num = input.readInt("Введите пункт меню");

        if (num == 1) {
            int accId = input.readInt("Номер счета");
            LocalDateTime from = input.readDate("Период с (ДД.ММ.ГГГГ)");
            LocalDateTime to = input.readDate("Период до (ДД.ММ.ГГГГ)");

            PeriodicReport pr = reportService.reportDiffForSelectedPeriod(accId, from, to);
            System.out.println("ОТЧЕТ О ДОХОДАХ И РАСХОДАХ ЗА ПЕРИОД С " + pr.from() + " ПО " + pr.to());
            System.out.println("Общий доход: " + pr.totalIncome());
            System.out.println("Общие траты: " + pr.totalExpense());
            System.out.println("Разница: " + pr.difference());
        } else if (num == 2) {
            int accId = input.readInt("Номер счета");
            int typeId = input.readInt("Тип (1 - Доход, 2 - Расход)");

            GroupedCategoriesReport gcr = reportService.reportGroupedCategories(accId,
                    OperationType.fromInt(typeId - 1));
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

package tiger.service.facade;

import org.springframework.stereotype.Component;
import tiger.model.requests.report.GroupedReportRequest;
import tiger.model.requests.report.PeriodicReportRequest;
import tiger.service.ReportService;
import tiger.service.dto.GroupedCategoriesReport;
import tiger.service.dto.PeriodicReport;

@Component
public class ReportFacade {
    private final ReportService reportService;

    public ReportFacade(ReportService reportService) {
        this.reportService = reportService;
    }

    public PeriodicReport getPeriodicReport(PeriodicReportRequest request) {
        return reportService.reportDiffForSelectedPeriod(request.accountId(), request.from(), request.to());
    }

    public GroupedCategoriesReport getGroupedReport(GroupedReportRequest request) {
        return reportService.reportGroupedCategories(request.accountId(), request.type());
    }
}

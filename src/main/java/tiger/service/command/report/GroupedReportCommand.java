package tiger.service.command.report;

import tiger.model.requests.report.GroupedReportRequest;
import tiger.service.command.Command;
import tiger.service.dto.GroupedCategoriesReport;
import tiger.service.facade.ReportFacade;

public class GroupedReportCommand implements Command {
    private final ReportFacade facade;
    private final GroupedReportRequest request;
    private GroupedCategoriesReport result;

    public GroupedReportCommand(ReportFacade facade, GroupedReportRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        this.result = facade.getGroupedReport(request);
    }

    @Override
    public String getName() {
        return "Аналитика: Группировка по категориям";
    }

    public GroupedCategoriesReport getResult() {
        return result;
    }
}

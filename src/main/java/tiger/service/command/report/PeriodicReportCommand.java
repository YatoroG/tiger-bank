package tiger.service.command.report;

import tiger.model.requests.report.PeriodicReportRequest;
import tiger.service.command.ICommand;
import tiger.service.dto.PeriodicReport;
import tiger.service.facade.ReportFacade;

public class PeriodicReportCommand implements ICommand {
    private final ReportFacade facade;
    private final PeriodicReportRequest request;
    private PeriodicReport result;

    public PeriodicReportCommand(ReportFacade facade, PeriodicReportRequest request) {
        this.facade = facade;
        this.request = request;
    }

    @Override
    public void execute() {
        this.result = facade.getPeriodicReport(request);
    }

    @Override
    public String getName() {
        return "Аналитика: Разница за период";
    }

    public PeriodicReport getResult() {
        return result;
    }
}

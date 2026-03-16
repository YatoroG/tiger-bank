package tiger.service.command.exchange;

import tiger.service.command.ICommand;
import tiger.service.file.exchange.ExchangeService;

public class ExportCommand implements ICommand {
    private final ExchangeService service;
    private final String format;

    public ExportCommand(ExchangeService service, String format) {
        this.service = service;
        this.format = format;
    }

    @Override
    public void execute() {
        service.performExport(format);
    }

    @Override
    public String getName() {
        return "Экспорт данных в " + format.toUpperCase();
    }
}

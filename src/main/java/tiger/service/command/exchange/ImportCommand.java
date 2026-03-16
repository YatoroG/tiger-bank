package tiger.service.command.exchange;

import tiger.service.command.ICommand;
import tiger.service.file.exchange.ExchangeService;

public class ImportCommand implements ICommand {
    private final ExchangeService service;
    private final String format;

    public ImportCommand(ExchangeService service, String format) {
        this.service = service;
        this.format = format;
    }

    @Override
    public void execute() {
        service.performImport(format);
    }

    @Override
    public String getName() {
        return "Импорт данных из " + format.toUpperCase();
    }
}

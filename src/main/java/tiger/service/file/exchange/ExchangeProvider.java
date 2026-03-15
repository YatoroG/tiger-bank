package tiger.service.file.exchange;

import java.io.File;
import java.io.IOException;
import tiger.model.dto.DataAggregator;

public abstract class ExchangeProvider implements IExchangeProvider {
    @Override
    public final void exportData(DataAggregator data) {
        try {
            File file = new File("tiger." + getFormat());
            writeData(data, file);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось сохранить в " + getFormat(), e);
        }
    }

    @Override
    public final DataAggregator importData() {
        try {
            File file = new File("tiger." + getFormat());
            if (!file.exists()) throw new IOException("Ошибка: Файл не найден");
            return readData(file);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось прочитать " + getFormat(), e);
        }
    }

    protected abstract void writeData(DataAggregator data, File file) throws IOException;
    protected abstract DataAggregator readData(File file) throws IOException;
}

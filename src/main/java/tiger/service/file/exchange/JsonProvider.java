package tiger.service.file.exchange;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import tiger.model.dto.DataAggregator;

@Component
public class JsonProvider implements IExchangeProvider {
    private final ObjectMapper objectMapper;

    public JsonProvider() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void exportData(DataAggregator data) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("tiger.json"), data);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось сохранить в JSON (" + e.getMessage() + ")");
        }
    }

    @Override
    public DataAggregator importData() {
        try {
            return objectMapper.readValue(new File("tiger.json"), DataAggregator.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось прочитать JSON (" + e.getMessage() + ")");
        }
    }

    @Override
    public String getFormat() {
        return "json";
    }
}

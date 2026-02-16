package tiger.service.file.exchange;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import tiger.model.dto.DataAggregator;

@Component
public class YamlProvider implements IExchangeProvider {
    private final ObjectMapper objectMapper;

    public YamlProvider() {
        this.objectMapper = new ObjectMapper(new YAMLFactory())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void exportData(DataAggregator data) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("tiger.yml"), data);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось сохранить в YAML (" + e.getMessage() + ")");
        }
    }

    @Override
    public DataAggregator importData() {
        try {
            return objectMapper.readValue(new File("tiger.yml"), DataAggregator.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: Не удалось прочитать YAML (" + e.getMessage() + ")");
        }
    }

    @Override
    public String getFormat() {
        return "yaml";
    }
}

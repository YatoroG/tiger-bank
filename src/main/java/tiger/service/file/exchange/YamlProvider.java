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
public class YamlProvider extends ExchangeProvider {
    private final ObjectMapper objectMapper;

    public YamlProvider() {
        this.objectMapper = new ObjectMapper(new YAMLFactory())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void writeData(DataAggregator data, File file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }

    @Override
    protected DataAggregator readData(File file) throws IOException {
        return objectMapper.readValue(file, DataAggregator.class);
    }

    @Override
    public String getFormat() {
        return "yaml";
    }
}

package tiger.service.file.exchange;

import tiger.model.dto.DataAggregator;

public interface IExchangeProvider {
    void exportData(DataAggregator data);
    DataAggregator importData();
    String getFormat();
}

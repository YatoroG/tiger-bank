package tiger.service.file.exchange;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.dto.DataAggregator;
import tiger.repository.BankAccountRepository;
import tiger.repository.CategoryRepository;
import tiger.repository.OperationRepository;
import tiger.service.BankAccountService;
import tiger.service.CategoryService;
import tiger.service.OperationService;

@Service
public class ExchangeService {
    private final BankAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final OperationRepository operationRepo;

    private final BankAccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;

    private final IExchangeProvider csvProvider;
    private final JsonProvider jsonProvider;
    private final YamlProvider yamlProvider;

    public ExchangeService(BankAccountRepository accountRepo, CategoryRepository categoryRepo,
                           OperationRepository operationRepo, BankAccountService accountService,
                           CategoryService categoryService, OperationService operationService,
                           IExchangeProvider csvProvider, JsonProvider jsonProvider,
                           YamlProvider yamlProvider) {
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
        this.csvProvider = csvProvider;
        this.jsonProvider = jsonProvider;
        this.yamlProvider = yamlProvider;
    }

    public void performCsvImport() {
        DataAggregator importedData = csvProvider.importData();
        renewNextIds(importedData);
        System.out.println("Импорт CSV завершен успешно. Восстановлено объектов: " +
                (importedData.getAccounts().size() +
                        importedData.getCategories().size() +
                        importedData.getOperations().size()));
    }

    public void performCsvExport() {
        DataAggregator dataToExport = getDataAggregator();

        try {
            csvProvider.exportData(dataToExport);
            System.out.println("Данные успешно экспортированы в CSV");
        } catch (Exception e) {
            System.err.println("Ошибка: Не удалось выполнить экспорт: " + e.getMessage());
        }
    }

    public void performJsonImport() {
        DataAggregator importedData = jsonProvider.importData();
        renewNextIds(importedData);
        System.out.println("Импорт JSON завершен успешно. Восстановлено объектов: " +
                        (importedData.getAccounts().size() + importedData.getCategories().size() +
                        importedData.getOperations().size()));
    }

    public void performJsonExport() {
        DataAggregator dataToExport = getDataAggregator();

        try {
            jsonProvider.exportData(dataToExport);
            System.out.println("Данные успешно экспортированы в JSON");
        } catch (Exception e) {
            System.err.println("Ошибка: Не удалось выполнить экспорт: " + e.getMessage());
        }
    }

    public void performYamlImport() {
        DataAggregator importedData = yamlProvider.importData();
        renewNextIds(importedData);
        System.out.println("Импорт YAML завершен успешно. Восстановлено объектов: " +
                        (importedData.getAccounts().size() + importedData.getCategories().size() +
                        importedData.getOperations().size()));
    }

    public void performYamlExport() {
        DataAggregator dataToExport = getDataAggregator();

        try {
            yamlProvider.exportData(dataToExport);
            System.out.println("Данные успешно экспортированы в YAML");
        } catch (Exception e) {
            System.err.println("Ошибка: Не удалось выполнить экспорт: " + e.getMessage());
        }
    }

    private DataAggregator getDataAggregator() {
        List<BankAccount> accounts = new ArrayList<>(accountRepo.getAllAccounts().values());
        List<Category> categories = new ArrayList<>(categoryRepo.getAllCategories().values());
        List<Operation> operations = new ArrayList<>(operationRepo.getAllOperations().values());

        DataAggregator dataToExport = new DataAggregator();
        dataToExport.setAccounts(accounts);
        dataToExport.setCategories(categories);
        dataToExport.setOperations(operations);
        return dataToExport;
    }

    private void renewNextIds(DataAggregator importedData) {
        int maxAccId = 0;

        for (BankAccount acc : importedData.getAccounts()) {
            accountRepo.add(acc);
            if (acc.getAccountId() > maxAccId) maxAccId = acc.getAccountId();
        }
        accountService.checkNextId(maxAccId);

        int maxCatId = 0;
        for (Category cat : importedData.getCategories()) {
            categoryRepo.add(cat);
            if (cat.getCategoryId() > maxCatId) maxCatId = cat.getCategoryId();
        }
        categoryService.checkNextId(maxCatId);

        int maxOpId = 0;
        for (Operation op : importedData.getOperations()) {
            operationRepo.add(op);
            if (op.getOperationId() > maxOpId) maxOpId = op.getOperationId();
        }
        operationService.checkNextId(maxOpId);
    }
}

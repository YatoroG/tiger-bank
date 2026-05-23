package tiger.service.file.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    private final Map<String, IExchangeProvider> providers;

    public ExchangeService(BankAccountRepository accountRepo, CategoryRepository categoryRepo,
                           OperationRepository operationRepo, BankAccountService accountService,
                           CategoryService categoryService, OperationService operationService,
                           List<IExchangeProvider> providerList) {
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
        this.providers = providerList.stream()
                .collect(Collectors.toMap(IExchangeProvider::getFormat, p -> p));
    }

    public void performImport(String format) {
        IExchangeProvider provider = getProvider(format);
        DataAggregator importedData = provider.importData();
        renewNextIds(importedData);

        System.out.println("Импорт " + format.toUpperCase() + " завершен успешно. Восстановлено объектов: " +
                (importedData.getAccounts().size() + importedData.getCategories().size() +
                        importedData.getOperations().size()));
    }

    public void performExport(String format) {
        IExchangeProvider provider = getProvider(format);
        DataAggregator dataToExport = getDataAggregator();

        try {
            provider.exportData(dataToExport);
            System.out.println("Данные успешно экспортированы в " + format.toUpperCase());
        } catch (Exception e) {
            System.err.println("Ошибка: Не удалось выполнить экспорт в " + format + ": " + e.getMessage());
        }
    }

    private IExchangeProvider getProvider(String format) {
        IExchangeProvider provider = providers.get(format.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Формат " + format + " не поддерживается");
        }
        return provider;
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

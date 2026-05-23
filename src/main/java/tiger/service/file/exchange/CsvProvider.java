package tiger.service.file.exchange;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.model.dto.DataAggregator;

@Component
public class CsvProvider implements IExchangeProvider {
    @Override
    public void exportData(DataAggregator data) {
        exportBankAccounts(data);
        exportOperations(data);
        exportCategory(data);
    }

    private void exportBankAccounts(DataAggregator data) {
        try (BufferedWriter accountWriter = new BufferedWriter(new FileWriter("accounts.csv"))) {
            accountWriter.write("id,name,balance");
            accountWriter.newLine();

            for (BankAccount account : data.getAccounts()) {
                accountWriter.write(String.format("%d,%s,%s", account.getAccountId(),
                        account.getName(), account.getBalance()));
                accountWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка: Не удалось сохранить счета в CSV (" + e.getMessage() + ")");
        }
    }

    private void exportOperations(DataAggregator data) {
        try (BufferedWriter operationWriter = new BufferedWriter(new FileWriter("operations.csv"))) {
            operationWriter.write("id,type,account_id,amount,date,desc,category_id");
            operationWriter.newLine();

            for (Operation operation : data.getOperations()) {
                operationWriter.write(String.format("%d,%s,%d,%s,%s,%s,%d",
                        operation.getOperationId(), operation.getType(), operation.getBankAccountId(),
                        operation.getAmount(), operation.getDate(), operation.getDescription(),
                        operation.getCategoryId()));
                operationWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка: Не удалось сохранить операции в CSV (" + e.getMessage() + ")");
        }
    }

    private void exportCategory(DataAggregator data) {
        try (BufferedWriter categoryWriter = new BufferedWriter(new FileWriter("categories.csv"))) {
            categoryWriter.write("id,name,type");
            categoryWriter.newLine();

            for (Category category : data.getCategories()) {
                categoryWriter.write(String.format("%d,%s,%s", category.getCategoryId(),
                        category.getName(), category.getCategoryType().name()));
                categoryWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка: Не удалось сохранить категории в CSV (" + e.getMessage() + ")");
        }
    }

    @Override
    public DataAggregator importData() {
        DataAggregator data = new DataAggregator();
        try {
            data.setCategories(importCategories());
            data.setAccounts(importBankAccounts());
            data.setOperations(importOperations());
        } catch (IOException | RuntimeException e) {
            System.err.println("Ошибка: Невозможно выполнить импорт (" + e.getMessage() + ")");
            throw new RuntimeException("Импорт прерван");
        }

        return data;
    }

    @Override
    public String getFormat() {
        return "csv";
    }

    private List<Category> importCategories() throws IOException {
        List<Category> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("categories.csv"))) {
            String line = reader.readLine();
            if (line == null) throw new IOException("Файл categories.csv пуст");

            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                Category category = new Category(Integer.parseInt(col[0]), col[1],
                        OperationType.fromStr(col[2]));
                list.add(category);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Категории");
        }
        return list;
    }

    private List<BankAccount> importBankAccounts() throws IOException {
        List<BankAccount> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.csv"))) {
            String line = reader.readLine();
            if (line == null) throw new IOException("Файл accounts.csv пуст");

            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                BankAccount acc = new BankAccount(Integer.parseInt(col[0]), col[1], new BigDecimal(col[2]));
                list.add(acc);
            }
        }
        return list;
    }

    private List<Operation> importOperations() throws IOException {
        List<Operation> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("operations.csv"))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                Operation op = new Operation(
                        Integer.parseInt(col[0]),
                        OperationType.fromStr(col[1]),
                        Integer.parseInt(col[2]),
                        new BigDecimal(col[3]),
                        LocalDateTime.parse(col[4]),
                        col[5],
                        Integer.parseInt(col[6])
                );
                list.add(op);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Операции");
        }
        return list;
    }
}

package tiger.service.file.exchange;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import tiger.model.BankAccount;
import tiger.model.Category;
import tiger.model.Operation;
import tiger.model.OperationType;
import tiger.model.dto.DataAggregator;

@Component
public class CsvProvider extends ExchangeProvider {
    @Override
    protected void writeData(DataAggregator data, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            exportBankAccounts(writer, data.getAccounts());
            exportCategories(writer, data.getCategories());
            exportOperations(writer, data.getOperations());
        }
    }

    @Override
    protected DataAggregator readData(File file) throws IOException {
        DataAggregator data = new DataAggregator();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            importFile(reader, data);
        }
        return data;
    }

    @Override
    public String getFormat() {
        return "csv";
    }

    private void exportBankAccounts(PrintWriter writer, List<BankAccount> accounts) {
        writer.println("SECTION:ACCOUNTS");
        writer.println("id,name,balance");
        accounts.forEach(a -> writer.printf("%d,%s,%s%n",
                a.getAccountId(), a.getName(),
                a.getBalance()));
    }

    private void exportOperations(PrintWriter writer, List<Operation> operations) {
        writer.println("SECTION:OPERATIONS");
        writer.println("id,type,account_id,amount,date,desc,category_id");
        operations.forEach(o -> writer.printf("%d,%s,%d,%s,%s,%s,%d%n",
                o.getOperationId(), o.getType(), o.getBankAccountId(),
                o.getAmount(), o.getDate(), o.getDescription(),
                o.getCategoryId()));
    }

    private void exportCategories(PrintWriter writer, List<Category> categories) {
        writer.println("SECTION:CATEGORIES");
        writer.println("id,name,type");
        categories.forEach(c -> writer.printf("%d,%s,%s%n",
                c.getCategoryId(), c.getName(),
                c.getCategoryType().name()));
    }

    private void importFile(BufferedReader reader, DataAggregator data) throws IOException {
        String line;
        String currentSection = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("SECTION:")) {
                currentSection = line;
                continue;
            }

            if (line.startsWith("id,")) {
                continue;
            }

            switch (currentSection) {
                case "SECTION:ACCOUNTS" -> data.getAccounts().add(importBankAccounts(line));
                case "SECTION:CATEGORIES" -> data.getCategories().add(importCategories(line));
                case "SECTION:OPERATIONS" -> data.getOperations().add(importOperations(line));
            }
        }
    }

    private Category importCategories(String line) throws IOException {
        String[] col = line.split(",");
        return new Category(Integer.parseInt(col[0]), col[1],
                OperationType.fromStr(col[2]));
    }

    private BankAccount importBankAccounts(String line) throws IOException {
        String[] col = line.split(",");
        return new BankAccount(Integer.parseInt(col[0]), col[1], new BigDecimal(col[2]));
    }

    private Operation importOperations(String line) throws IOException {
        String[] col = line.split(",");
        return new Operation(
                Integer.parseInt(col[0]),
                OperationType.fromStr(col[1]),
                Integer.parseInt(col[2]),
                new BigDecimal(col[3]),
                LocalDateTime.parse(col[4]),
                col[5],
                Integer.parseInt(col[6])
        );
    }
}

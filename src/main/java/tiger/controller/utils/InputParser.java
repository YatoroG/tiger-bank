package tiger.controller.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class InputParser {
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public int readInt(String message) {
        System.out.print(message + ": ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: не число");
            return readInt(message);
        }
    }

    public BigDecimal readBigDecimal(String message) {
        System.out.print(message + ": ");
        return new BigDecimal(scanner.nextLine());
    }

    public String readString(String message) {
        System.out.print(message + ": ");
        return scanner.nextLine();
    }

    public LocalDateTime readDate(String message) {
        System.out.print(message + ": ");
        String input = scanner.nextLine();
        try {
            return LocalDate.parse(input, DATE_FORMATTER).atStartOfDay();
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: неверный формат даты");
            return readDate(message);
        }
    }
}

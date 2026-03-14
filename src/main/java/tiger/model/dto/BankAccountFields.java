package tiger.model.dto;

import java.math.BigDecimal;

public record BankAccountFields(int id, String name, BigDecimal balance) {
}

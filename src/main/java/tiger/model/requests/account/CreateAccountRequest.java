package tiger.model.requests.account;

import java.math.BigDecimal;

public record CreateAccountRequest(String name, BigDecimal balance) {
}

package tiger.service.factory;

import java.math.BigDecimal;
import tiger.model.BankAccount;

public interface IBankAccountFactory {
    BankAccount createAccount(String name, BigDecimal balance);
    void checkNextId(int maxAccId);
}

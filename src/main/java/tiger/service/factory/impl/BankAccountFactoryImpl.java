package tiger.service.factory.impl;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import tiger.model.BankAccount;
import tiger.service.factory.IBankAccountFactory;

@Component
public class BankAccountFactoryImpl implements IBankAccountFactory {
    private int nextAccountId = 1;

    @Override
    public BankAccount createAccount(String name, BigDecimal balance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: Название счета не может быть пустым");
        }

        return (balance == null) ? new BankAccount(nextAccountId++, name)
                : new BankAccount(nextAccountId++, name, balance);
    }

    @Override
    public void checkNextId(int maxAccId) {
        this.nextAccountId = maxAccId + 1;
    }
}

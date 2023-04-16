package mkaciuba.inggreencode.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class AccountBalance {
    private String account;
    private int debitCount;
    private int creditCount;
    private BigDecimal balance;

    public static AccountBalance of(String account) {
        return new AccountBalance(account, 0, 0, BigDecimal.ZERO);
    }

    public void credit(BigDecimal amount) {
        creditCount++;
        balance = balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        debitCount++;
        balance = balance.subtract(amount);
    }
}

package mkaciuba.inggreencode.transactions.resource;

import mkaciuba.inggreencode.transactions.dto.AccountBalance;
import mkaciuba.inggreencode.transactions.dto.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/transactions/report")
public class TransactionsReportResource {

    @PostMapping
    public List<AccountBalance> createReport(@RequestBody List<Transaction> transactions) {
        var accountMap = new HashMap<String, AccountBalance>();

        for (var transaction : transactions) {
            var debitAccount = transaction.debitAccount();
            var creditAccount = transaction.creditAccount();
            var amount = transaction.amount();

            accountMap.computeIfAbsent(debitAccount, AccountBalance::of);
            accountMap.computeIfAbsent(creditAccount, AccountBalance::of);

            accountMap.get(creditAccount).credit(amount);
            accountMap.get(debitAccount).debit(amount);
        }

        return accountMap.values()
                .stream()
                .sorted(Comparator.comparing(AccountBalance::getAccount))
                .toList();
    }
}

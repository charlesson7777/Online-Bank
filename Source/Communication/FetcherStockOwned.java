 /*
 * FetcherStocks.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Fetches and contains stocks
 */
package Communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BankObjects.BankAccount;
import BankObjects.StockOwned;
import BankObjects.TypeAccount;

public class FetcherStockOwned extends FetcherClient {
    List<StockOwned> stocks;
    FetcherAccounts fetcherAccounts;

    public FetcherStockOwned() {
        super();
        fetcherAccounts = new FetcherAccounts();
        stocks = new ArrayList<>();
    }

    @Override
    public void fetch(UUID uid) {
        fetcherAccounts.fetch(uid);
        List<BankAccount> securities = fetcherAccounts.getAccountsOfType(TypeAccount.SECURITIES);

        for (BankAccount account : securities) {
            List<StockOwned> toAdd = db.getAllStockOwnedByAccount(account.getID());
            if (!toAdd.isEmpty()) {
                stocks.addAll(toAdd);
            }
        }

    }

    public List<StockOwned> getStocks() {
        return stocks;
    }
    
}

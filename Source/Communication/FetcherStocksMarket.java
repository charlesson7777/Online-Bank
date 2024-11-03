 /*
 * FetcherStocks.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Fetches and contains stocks
 */
package Communication;

import java.util.List;
import java.util.UUID;
import BankObjects.Stock;

public class FetcherStocksMarket extends FetcherClient {
    List<Stock> stocks;

    public FetcherStocksMarket() {
        super();
    }

    @Override
    public void fetch(UUID uid) {
        stocks = db.fetchAllStocks();
    }

    public List<Stock> getStocks() {
        return stocks;
    }
    
}

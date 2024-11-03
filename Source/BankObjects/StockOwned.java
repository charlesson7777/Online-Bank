 /*
 * StockOwned.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a stock that is owned by a client
 */
package BankObjects;
import java.util.UUID;

public class StockOwned {
    private UUID soId;
    private long accountId;
    private UUID sid;
    private String name;
    private double purchasePrice;
    private double soldPrice;
    private int numofshare;
    private Currency currency;

    public StockOwned(UUID soId, long accountId, double purchasePrice, String name, int numofshare, Currency currency, UUID sid) {
        this.soId = soId;
        this.accountId = accountId;
        this.purchasePrice = purchasePrice;
        this.name = name;
        this.numofshare = numofshare;
        this.sid = sid;
        soldPrice = 0.00;
    }

    public String getName() {
        return name;
    }

    public UUID getSoId() {
        return soId;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public UUID getSid() {
        return sid;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getNumOfShare() {
        return numofshare;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public long getAccountId() {
        return accountId;
    }

    // communicate with db
    public double getCurrentPrice() {
        return 0;
    }
    
}

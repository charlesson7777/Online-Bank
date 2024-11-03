 /*
 * TransactionDeposit.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a deposit transaction
 */

package BankObjects;
import java.util.UUID;

public class TransactionDeposit extends Transaction {
    private long aid;

    public TransactionDeposit(double amount, Currency currency, long id, UUID tid, UUID uid) {
        super(TypeTransaction.DEPOSIT, amount, currency, tid, uid);
        this.aid = id;
    }

    public void setAccountID(long id) {
        aid = id;
    }

    public long getAccountID() {
        return aid;
    }
    
}

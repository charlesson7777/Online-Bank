 /*
 * TransactionWithdrawal.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a withdrawal transaction
 */
package BankObjects;
import java.util.UUID;

public class TransactionWithdrawal extends Transaction {
    private long aid;

    public TransactionWithdrawal(double amount, Currency currency, long id, UUID tid, UUID uid) {
        super(TypeTransaction.WITHDRAWAL, amount, currency, tid, uid);
        this.aid = id;
    }

    public void setAccountID(long id) {
        aid = id;
    }

    public long getAccountID() {
        return aid;
    }
}

 /*
 * TransactionTransfer.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a transfer transaction
 */
package BankObjects;
import java.util.UUID;

public class TransactionTransfer extends Transaction {
    private long senderID;
    private long recipientID;

    public TransactionTransfer(double amount, Currency currency, long senderID, long recipientID, UUID tid, UUID uid) {
        super(TypeTransaction.TRANSFER, amount, currency, tid, uid);
        this.senderID = senderID;
        this.recipientID = recipientID;
    }

    public void setRecipientID(long id) {
        recipientID = id;
    }

    public void setSenderID(long id) {
        senderID = id;
    }

    public long getRecipientID() {
        return recipientID;
    }

    public long getSenderID() {
        return senderID;
    }
}

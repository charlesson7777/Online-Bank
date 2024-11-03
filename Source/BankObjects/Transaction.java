 /*
 * Transaction.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent any type of transaction
 */
package BankObjects;
import java.sql.Date;
import java.util.UUID;

public abstract class Transaction {
    protected long timestamp;
    protected UUID tid;
    protected TypeTransaction type;
    protected String desccription;
    protected double amount;
    protected Currency currency;
    protected Date date;
    protected UUID uid;

    public Transaction(TypeTransaction type, double amount, Currency currency, UUID tid, UUID uid) {
        timestamp = System.currentTimeMillis();
        date = new Date(timestamp);
        this.tid = tid;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    } 

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getID() {
        return tid;
    }

    public UUID getUserId() {
        return uid;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }

    public TypeTransaction getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public void setDescription(String desc) {
        desccription = desc;
    }

    public String getDescription() {
        return desccription;
    }

    public Currency getCurrency() {
        return currency;
    }

    
}

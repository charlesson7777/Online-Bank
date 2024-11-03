 /*
 * BankAccount.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * A generic bank account 
 */

package BankObjects;
import java.util.UUID;

public abstract class BankAccount implements Comparable<BankAccount> {
    protected UUID userID;
    protected long aid;
    protected TypeAccount type;
    protected Currency currency;
    protected double balance;
    protected final double WITHDRAWAL_FEE = 0.01;

    public BankAccount(UUID id) {
        userID = id;
    }

    public void setAccountNumber(long num) {
        aid = num;
    }

    public void setCurrency(Currency c) {
        currency = c;
    }

    public void setBalance(double b) {
        balance = b;
    }

    public UUID getUserID() {
        return userID;
    }

    public long getID() {
        return aid;
    }

    public TypeAccount getAccountType() {
        return type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public String toString() {
        String out = ""; 
        out += "User ID: " + userID + "\n";
        out += "Account ID: " + aid + "\n";
        out += "Type: " + type + " | Currency: " + currency.getName() + " Balance: " + balance;


        return out;
    }

    public int compareTo(BankAccount account) {
        return Double.compare(this.balance, account.balance);
    }


}
 /*
 * FetcherTransactions.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Fetches and contains transactions
 */
package Communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import BankObjects.Transaction;

public class FetcherTransactions extends FetcherClient {
    private List<Transaction> dailyTxns;
    private List<Transaction> allTxns;
    private Date currentDate;
    
    public FetcherTransactions() {
        super();
        dailyTxns = new ArrayList<>();
        allTxns = new ArrayList<>();
        setDate();
    }

    public void setDate() {
        currentDate = new Date(System.currentTimeMillis());
    }

    
    public void fetch(UUID uid) {
        allTxns = db.getAllTransactionsOfUser(uid);
        Date last24hrs = new Date(currentDate.getTime() - (24 * 60 * 60 * 1000));
        
        for (Transaction txn : allTxns) {
            if (txn.getDate().compareTo(last24hrs) > 0) {
                dailyTxns.add(txn);
            }
        }

    }

    public List<Transaction> getDailyTxn() {
        return dailyTxns;
    }

    public List<Transaction> getAllTxn() {
        return allTxns;
    }
    
}

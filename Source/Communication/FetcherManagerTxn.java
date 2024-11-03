package Communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BankObjects.Transaction;

public class FetcherManagerTxn extends FetcherManager {
    List<Transaction> dailyTxn;

    public FetcherManagerTxn() {
        super();
        dailyTxn = new ArrayList<>();
    }

    @Override
    public void fetch(UUID uid) {
        dailyTxn = db.getDailyTransaction();
    }

    public List<Transaction> getDailyTxn() {
        return dailyTxn;
    }
    
}

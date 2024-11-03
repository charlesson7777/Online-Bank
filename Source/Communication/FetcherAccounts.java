 /*
 * FetcherAccounts.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Fetches and contains Bank Accounts
 */
package Communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BankObjects.BankAccount;
import BankObjects.BankAccountLoan;
import BankObjects.TypeAccount;

public class FetcherAccounts extends FetcherClient {
    protected List<BankAccount> allAccounts;
    protected List<TypeAccount> accountTypes;
    protected List<BankAccountLoan> loanAccounts;
    protected final int NUM_TOP_ACCOUNTS = 3;
    
    public FetcherAccounts() {
        super();
        accountTypes = new ArrayList<>();
        loanAccounts = new ArrayList<>();
        allAccounts = new ArrayList<>();
    }

    
    public void fetch(UUID uid) {
        allAccounts = db.getAllAccountsOfUser(uid);

        for (BankAccount account : allAccounts) {
            if (!accountTypes.contains(account.getAccountType())) {
                accountTypes.add(account.getAccountType());
            }
        }

        loanAccounts = db.getAllLoansOfUser(uid);
        if (!loanAccounts.isEmpty()) {
            accountTypes.add(TypeAccount.LOAN);
        }

    }

    public List<TypeAccount> getAccountTypes() {
        return accountTypes;
    }

    public List<BankAccount> getAccountsOfType(TypeAccount type) {
        List<BankAccount> toReturn = new ArrayList<>();
        for (BankAccount account : allAccounts) {
            if (account.getAccountType().equals(type)) {
                toReturn.add(account);
            }
        }

        return toReturn;
    }

    public List<BankAccountLoan> getLoanAccounts() {
        return loanAccounts;
    }

    public List<BankAccount> getNonLoanAccounts() {
        return allAccounts;
    }

    public List<BankAccount> getTopAccounts() {
        int i = NUM_TOP_ACCOUNTS;
        List<BankAccount> toReturn = new ArrayList<>();

        if (accountTypes.contains(TypeAccount.LOAN)) {
            for (BankAccount account : loanAccounts) {
                toReturn.add(account);
                i--;

                if (i == 0) {
                    break;
                }
            }
        }

        // fill the rest with non-loan accounts
        if (i > 0) {
            for (BankAccount account : allAccounts) {
                toReturn.add(account);
                i--;
    
                if (i == 0) {
                    break;
                }
            }

        }

        
        return toReturn;
    }
 
    
}

package Communication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import BankObjects.BankAccount;
import BankObjects.BankAccountLoan;
import BankObjects.TypeAccount;
import BankObjects.User;

public class FetcherManagerAccounts extends FetcherManager {
    protected List<BankAccount> allAccounts;
    protected List<TypeAccount> accountTypes;
    protected List<BankAccountLoan> loanAccounts;
    protected final int NUM_TOP_ACCOUNTS = 3;
    
    public FetcherManagerAccounts() {
        super();
        allAccounts = new ArrayList<>();
        accountTypes = new ArrayList<>();
        loanAccounts = new ArrayList<>();
    } 
    

    public void fetch(UUID uid) {
        allAccounts = db.getAllaccounts();

        for (BankAccount account : allAccounts) {
            if (!accountTypes.contains(account.getAccountType())) {
                accountTypes.add(account.getAccountType());
            }
        }

        loanAccounts = db.getAllLoans();
        if (!loanAccounts.isEmpty()) {
            accountTypes.add(TypeAccount.LOAN);
            Collections.sort(loanAccounts);
        }

    }
    
    public List<BankAccountLoan> getLoanAccounts() {
        return loanAccounts;
    }

    public List<BankAccount> getNonLoanAccounts() {
        return allAccounts;
    }

    public String[][] getTopLoans() {
        List<BankAccountLoan> topLoans = new ArrayList<>();
        int max = NUM_TOP_ACCOUNTS;

        for (BankAccountLoan account : loanAccounts) {
            topLoans.add(account);
            max--;
            if (max == 0) {
                break;
            }    
        }

        return formatToHome(topLoans);
    }

    private String[][] formatToHome(List<BankAccountLoan> list) {
        String[][] formattedList = new String[list.size()][3];
        int i = 0;

        for (BankAccount account : list) {
            User user = db.getUserByID(account.getUserID());
            String desc = user.getFirstName().toUpperCase() + " " + 
                user.getLastName().toUpperCase() + " (" + 
                user.getPrivilege() + ")";
            
            String balance = String.format("%.2f", account.getBalance());
                
            formattedList[i++] = new String[] {desc, balance, account.getCurrency().toString()};
        }

        return formattedList;
    }

}

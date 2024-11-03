 /*
 * FormCloseAccount.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for closing bank accounts
 */
package UI.Forms;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import BankObjects.BankAccount;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FormValidationClose;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormCloseAccount extends Form {
    protected JTextField amountField;
    protected JComboBox<String> currencyCombo, accountCombo;
    protected List<BankAccount> acclist;
    protected FetcherAccounts fetcher;

    public FormCloseAccount(InstanceManager manager, User user, FetcherAccounts fetcher) {
        super("Closing Accounts", manager);
        validation = new FormValidationClose(manager, user);
        this.fetcher = fetcher;
        initialize();
    }

    private void initialize() {
        
        setTitle("Close an account");

        addFormPrompt("Select Account to Close:");
        accountCombo = new MinimalDropDown<>(fetchAccounts()); 
        formBody.add(accountCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        submitButton.addActionListener(e -> processClose());
    }

    private String[] fetchAccounts() {
        acclist = fetcher.getNonLoanAccounts();
        String[] toReturn = new String[acclist.size()];
        for (int i = 0; i < acclist.size(); i++) {
            toReturn[i] = acclist.get(i).getAccountType() + " - " + 
            String.valueOf(acclist.get(i).getID()).substring(12) + 
            " (" + round(acclist.get(i).getBalance(), 2) + " " +  acclist.get(i).getCurrency().getName() + ")"; 
        }

        return toReturn;
    }


    private void processClose() {
        String accountNumber = String.valueOf(acclist.get(accountCombo.getSelectedIndex()).getID());

        boolean success = validation.process(new String[] {accountNumber});

        if (success) {
            this.dispose();
        }
    } 

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}

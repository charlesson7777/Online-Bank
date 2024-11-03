 /*
 * FormBuyStock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for deposits
 */

package UI.Forms;
import java.util.List;
import javax.swing.*;
import BankObjects.BankAccount;
import BankObjects.TypeCurrency;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FormValidationDeposit;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;


public class FormDeposit extends Form {
    protected JTextField amountField;
    protected MinimalDropDown<String> currencyCombo, accountNumberCombo;
    protected List<BankAccount> acclist;
    protected FetcherAccounts fetcher;

    public FormDeposit(InstanceManager manager, User user, FetcherAccounts fetcher) {
        super("New Deposit", manager);
        validation = new FormValidationDeposit(manager, user);
        this.fetcher = fetcher;
        initialize();
    }

    private void initialize() {
        setTitle("Deposit");

        addFormPrompt("Enter an amount to deposit:");

        amountField = addTextField();

        addFormPrompt("Select an account to deposit:");
        accountNumberCombo = new MinimalDropDown<>(fetchAccounts()); 
        formBody.add(accountNumberCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Select currency:");

        String[] currencies = TypeCurrency.getCurrencyStrArr();
        currencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(currencyCombo);
        submitButton.addActionListener(e -> onSubmit());

    }

    private String[] fetchAccounts() {
        acclist = fetcher.getNonLoanAccounts();
        String[] toReturn = new String[acclist.size()];
        for (int i = 0; i < acclist.size(); i++) {
            toReturn[i] = acclist.get(i).getAccountType() + " - " + 
            String.valueOf(acclist.get(i).getID()).substring(12);
        }

        return toReturn;
    }

    private void onSubmit() {
        String amount = amountField.getText();
        String accountNumber = String.valueOf(acclist.get(accountNumberCombo.getSelectedIndex()).getID());
        String currency = currencyCombo.getSelectedItem().toString();
        
        boolean success = validation.process(new String[] {amount, accountNumber, currency});

        if (success) {
            this.dispose();
        }

    }
}
   



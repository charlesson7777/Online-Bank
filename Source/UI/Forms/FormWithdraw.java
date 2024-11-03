 /*
 * FormWithdraw.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for withdrawing money
 */
package UI.Forms;

import java.util.List;

import javax.swing.*;

import BankObjects.BankAccount;
import BankObjects.TypeCurrency;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FormValidationWithdraw;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormWithdraw extends Form {
    protected JTextField amountField;
    protected JComboBox<String> currencyCombo, accountCombo;
    protected List<BankAccount> acclist;
    protected FetcherAccounts fetcher;

    public FormWithdraw(InstanceManager manager, User user, FetcherAccounts fetcher) {
        super("New Withdrawal Transaction", manager);
        validation = new FormValidationWithdraw(manager, user);
        this.fetcher = fetcher;
        initialize();
    }

    private void initialize() {
        setTitle("Withdrawal");

        addFormPrompt("Enter an amount to withdraw:");
        amountField = addTextField();

        addFormPrompt("Enter Account Number:");
        accountCombo = new MinimalDropDown<>(fetchAccounts()); 
        formBody.add(accountCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Select currency:");
        String[] currencies = TypeCurrency.getCurrencyStrArr();
        currencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(currencyCombo);

        submitButton.addActionListener(e -> processWithdrawal());
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


    private void processWithdrawal() {
        String amount = amountField.getText();
        String accountNumber = String.valueOf(acclist.get(accountCombo.getSelectedIndex()).getID());
        String currency = currencyCombo.getSelectedItem().toString();

        boolean success = validation.process(new String[] {amount, accountNumber, currency});

        if (success) {
            this.dispose();
        }
    }

}

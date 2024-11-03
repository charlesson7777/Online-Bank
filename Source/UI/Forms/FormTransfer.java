 /*
 * FormTransfer.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for transfer transactions
 */
package UI.Forms;

import java.util.List;

import javax.swing.*;

import BankObjects.BankAccount;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FormValidationTransfer;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormTransfer extends Form {

    // currency is set by the account you're sending from and to
    protected JTextField amountField, toAccountField;
    protected JComboBox<String> accountCombo;
    protected List<BankAccount> acclist;
    protected FetcherAccounts fetcher;

    public FormTransfer(InstanceManager manager, User user, FetcherAccounts fetcher) {
        super("New Transfer Transaction", manager);
        this.fetcher = fetcher;
        validation = new FormValidationTransfer(manager, user);
        initialize();
    }

    private void initialize() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        setTitle("Transfer");

        addFormPrompt("Enter an amount to transfer (sender currency):");
        amountField = addTextField();

        addFormPrompt("Select sender account to withdraw from:");
        accountCombo = new MinimalDropDown<>(fetchAccounts());
        formBody.add(accountCombo);
        formBody.add(Box.createVerticalStrut(margins));
    

        addFormPrompt("Enter account to transfer to:");
        toAccountField = addTextField();

        submitButton.addActionListener(e -> processTransfer());
    }

    private String[] fetchAccounts() {
        acclist = fetcher.getNonLoanAccounts();
        String[] toReturn = new String[acclist.size()];
        for (int i = 0; i < acclist.size(); i++) {
            toReturn[i] = acclist.get(i).getAccountType() + " - " + 
            String.valueOf(acclist.get(i).getID()).substring(12) + 
            " (" + acclist.get(i).getCurrency().getName() + ")";
        }

        return toReturn;
    }


    private void processTransfer() {
        String amount = amountField.getText();
        String sender = String.valueOf(acclist.get(accountCombo.getSelectedIndex()).getID());
        String recipient = toAccountField.getText();

        boolean success = validation.process(new String[] {amount, sender, recipient});

        if (success) {
            this.dispose();
        }

    }

    

}


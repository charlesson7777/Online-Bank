 /*
 * FormBuyStock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for opening new accounts
 */
package UI.Forms;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.User;
import Communication.FormValidationNewAccount;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormNewAccount extends Form {
    protected JTextField amountField;
    protected User user;
    protected JComboBox<String> typeCombo, currencyCombo;

    public FormNewAccount(InstanceManager manager, User user) {
        super("Create New Account", manager);
        this.user = user;
        validation = new FormValidationNewAccount(manager, user);
        initialize();
    }

    protected void initialize() {
        setTitle("New Account");
        
        addFormPrompt("Select account type:");
        typeCombo = new MinimalDropDown<>(TypeAccount.getAccountStrArr(user.getPrivilege()));
        formBody.add(typeCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Enter starting deposit amount:");
        amountField = addTextField();

        addFormPrompt("Select currency:");

        String[] currencies = TypeCurrency.getCurrencyStrArr();
        currencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(currencyCombo);

        submitButton.addActionListener(e -> process());
    }


    protected void process() {
        String accountType = (String) typeCombo.getSelectedItem();
        String amount = amountField.getText().trim().isEmpty() ? "" : amountField.getText().trim();
        String currency = (String) currencyCombo.getSelectedItem();

        boolean success = validation.process(new String[] {accountType, amount, currency});

        if (success) {
            this.dispose();
        }

    }
    
}

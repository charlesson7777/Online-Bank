package UI.Forms;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import BankObjects.TypeCurrency;
import Communication.FormValidationNewStock;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormNewStock extends Form {
    protected JTextField stockSymbolField, amountField, numSharesField;
    protected JComboBox<String> currencyCombo;

    public FormNewStock(InstanceManager manager) {
        super("New Stock Registration", manager);
        validation = new FormValidationNewStock(manager);
        initialize();
    }

    private void initialize() {
        setTitle("New Stock");

        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);

        addFormPrompt("Enter stock symbol:");
        stockSymbolField = addTextField();

        addFormPrompt("Enter price per stock:");
        amountField = addTextField();

        currencyCombo = new MinimalDropDown<>(TypeCurrency.getCurrencyStrArr());
        formBody.add(currencyCombo);
        formBody.add(Box.createVerticalStrut(margins));
        
        addFormPrompt("Enter number of shares:");
        numSharesField = addTextField();

        submitButton.addActionListener(e -> processCreation());
    }

    private void processCreation() {
        String stockSymbol = stockSymbolField.getText().toString();
        String pricePerStock = amountField.getText().toString();
        String currency = currencyCombo.getSelectedItem().toString();
        String numShares = numSharesField.getText().toString();

        boolean success = validation.process(new String[] {stockSymbol, pricePerStock, currency, numShares});

        if (success) {
            this.dispose();
        }

    }
    
}

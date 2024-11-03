package UI.Forms;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import BankObjects.Stock;
import Communication.FetcherStocksMarket;
import Communication.FormValidationModStock;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormModStock extends Form {
    protected JTextField amountField, numSharesField;
    protected JComboBox<String> currencyCombo, stockSymbolCombo;
    protected FetcherStocksMarket fetcher;
    protected List<Stock> stocks;

    public FormModStock(InstanceManager manager) {
        super("Form Modification", manager);
        fetcher = new FetcherStocksMarket();
        fetcher.fetch(null);
        validation = new FormValidationModStock(manager);
        initialize();
    }

    private void initialize() {
        setTitle("Modify a Stock");

        addFormPrompt("Select a stock:");
        stockSymbolCombo = new MinimalDropDown<>(fetchStocks());
        stockSymbolCombo.addActionListener(e -> updateFields());
        
        formBody.add(stockSymbolCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Update price per stock:");
        amountField = addTextField();

        submitButton.addActionListener(e -> processCreation());
    }

    private void updateFields() {
        if (!stocks.isEmpty()) {
            int stockIndex = stockSymbolCombo.getSelectedIndex();
            amountField.setText(String.valueOf(stocks.get(stockIndex).getPrice()));
        }

    }

    private String[] fetchStocks() {
        stocks = fetcher.getStocks();
        String[] toReturn = new String[stocks.size()];
        
        for (int i = 0; i < stocks.size(); i++) {
            toReturn[i] = stocks.get(i).getCode() + " (" + stocks.get(i).getCurrency() + ")";
        }

        return toReturn;
    }

    private void processCreation() {
        String stockSymbol = stockSymbolCombo.getSelectedItem().toString();
        String pricePerStock = amountField.getText().toString();
        String stockID = stocks.get(stockSymbolCombo.getSelectedIndex()).getID().toString();

        boolean success = validation.process(new String[] {stockSymbol, pricePerStock, stockID});

        if (success) {
            this.dispose();
        }

    }
    
}

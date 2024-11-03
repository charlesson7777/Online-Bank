 /*
 * FormBuyStock.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for buying stocks
 */
package UI.Forms;

import java.util.List;
import javax.swing.Box;
import javax.swing.JTextField;
import BankObjects.StockOwned;
import BankObjects.TypeAccount;
import BankObjects.BankAccount;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FetcherStockOwned;
import Communication.FormValidationSellStock;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormSellStock extends Form {
    protected User user;
    protected MinimalDropDown<String> stockCombo;
    protected List<StockOwned> stocklists;
    protected FetcherStockOwned fetcher;

    public FormSellStock(InstanceManager manager, User user, FetcherStockOwned f) {
        super("Sell Stocks", manager);
        this.user = user;
        fetcher = f;
        validation = new FormValidationSellStock(manager, user);
        initialize();
    }

    private void initialize() {
        setTitle("Sell Stocks");
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        
        addFormPrompt("Select stock symbol:");
        stockCombo = new MinimalDropDown<>(fetchStocks());
        formBody.add(stockCombo);
        formBody.add(Box.createVerticalStrut(margins));

        submitButton.addActionListener(e -> processBuy());
    }

    private String[] fetchStocks() {
        String[] toReturn = new String[] {""};
        stocklists = fetcher.getStocks();
        
        if (!stocklists.isEmpty()) {
            toReturn = new String[stocklists.size()];
        }

        for (int i = 0; i < stocklists.size(); i++) {
            StockOwned stock = stocklists.get(i);
            String name = stock.getName().substring(0, 1).toUpperCase() + 
                stock.getName().substring(1).toLowerCase();
            
            String numShares = Integer.toString(stock.getNumOfShare());

            toReturn[i] = name + " (" + numShares + ")";
        }

        return toReturn;
    }

    private void processBuy() {
        StockOwned stock = stocklists.get(stockCombo.getSelectedIndex());
        String stockID = stocklists.get(stockCombo.getSelectedIndex()).getSid().toString();

        boolean success = ((FormValidationSellStock)validation).process(stock);

        if (success) {
            this.dispose();
        }

    }
    
}

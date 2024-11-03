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
import BankObjects.Stock;
import BankObjects.TypeAccount;
import BankObjects.BankAccount;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FetcherStocksMarket;
import Communication.FormValidationBuyStock;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormBuyStock extends Form {
    protected JTextField numSharesField;
    protected User user;
    protected MinimalDropDown<String> stockCombo, securitiesCombo;
    protected List<Stock> stocklists;
    protected List<BankAccount> securitieslist;

    public FormBuyStock(InstanceManager manager, User user) {
        super("Buy a Stock", manager);
        this.user = user;
        validation = new FormValidationBuyStock(manager, user);
        initialize();
    }

    private void initialize() {
        setTitle("Buy Stocks");
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        
        addFormPrompt("Select stock symbol:");
        stockCombo = new MinimalDropDown<>(fetchStocks());
        formBody.add(stockCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Select securities account to withdraw from:");
        securitiesCombo = new MinimalDropDown<>(fetchSecurities());
        formBody.add(securitiesCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Enter number of shares to buy:");
        numSharesField = addTextField();
        formBody.add(numSharesField);

        submitButton.addActionListener(e -> processBuy());
    }

    private String[] fetchStocks() {
        String[] toReturn = new String[] {""};
        FetcherStocksMarket fetcher = new FetcherStocksMarket();
        fetcher.fetch(null);
        stocklists = fetcher.getStocks();
        
        if (!stocklists.isEmpty()) {
            toReturn = new String[stocklists.size()];
        }

        for (int i = 0; i < stocklists.size(); i++) {
            Stock stock = stocklists.get(i);
            String name = stock.getName().substring(0, 1).toUpperCase() + 
                stock.getName().substring(1).toLowerCase();
            
            String price = String.format("%.2f", stock.getPrice());

            toReturn[i] = name + " (" + price + " " + stock.getCurrency().getName() + ")";
        }

        return toReturn;
    }

    private String[] fetchSecurities() {        
        String[] toReturn = new String[] {""};
        FetcherAccounts fetcher = new FetcherAccounts();
        fetcher.fetch(user.getID());
        securitieslist = fetcher.getAccountsOfType(TypeAccount.SECURITIES);

        for (int i = 0; i < securitieslist.size(); i++) {
            toReturn[i] = securitieslist.get(i).getAccountType() + " - " + 
            String.valueOf(securitieslist.get(i).getID()).substring(12) + 
            " (" + securitieslist.get(i).getCurrency().getName() + ")";
        }

        return toReturn;
    }


    private void processBuy() {
        String stockID = stocklists.get(stockCombo.getSelectedIndex()).getID().toString();
        String securitiesID = String.valueOf(securitieslist.get(securitiesCombo.getSelectedIndex()).getID());
        String numShares = numSharesField.getText().toString();

        boolean success = validation.process(new String[] {stockID, securitiesID, numShares});

        if (success) {
            this.dispose();
        }

    }
    
}

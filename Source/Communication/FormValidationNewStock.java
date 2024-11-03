 /*
 * FormValidationNewStock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from manager adding new stocks form
 */
package Communication;


import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;

import BankObjects.Currency;
import BankObjects.Stock;
import BankObjects.TypeCurrency;

public class FormValidationNewStock extends FormValidationManager {
    private final int MIN_PRICE = 0;

    public FormValidationNewStock(InstanceManager manager) {
        super(manager);
    }

    

    @Override
    public boolean process(String[] response) {
        boolean toReturn;
        try {
            String stockSymbol = response[0].trim().toUpperCase();
            double pricePerStock = round(Double.parseDouble(response[1].trim()), 2);
            int currencyID = TypeCurrency.parseCurrency(response[2]).getId();
            Currency currency = db.getCurrencyById(currencyID);
            int numShares = Integer.parseInt(response[3].trim());

            if (isUniqueSymbol(stockSymbol)) {
                if (pricePerStock > MIN_PRICE) {
                    Stock stock = new Stock(stockSymbol, numShares, pricePerStock, currency);
                    stock.setSid(UUID.randomUUID());
                    stock.setCode(stockSymbol);
                    if (managerDB.addNewStock(stock)) {
                            JOptionPane.showMessageDialog(null, "Successfully added " + stockSymbol + " stock.", 
                        "New Stock Added", JOptionPane.INFORMATION_MESSAGE);
                        toReturn = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error. Please try again.", 
                    "Error Adding Stock", JOptionPane.ERROR_MESSAGE);
                        toReturn = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Price per stock should be greater than zero", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }

            } else {
                JOptionPane.showMessageDialog(null, "A stock of the same name already exists. Please try another name.", 
                    "Duplicate Stock", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a double for price and an integer for number of shares.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid currency.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
        
        return toReturn;
    }

    private boolean isUniqueSymbol(String symbol) {
        List<Stock> stocks = db.fetchAllStocks();
        boolean toReturn = true;

        if (!stocks.isEmpty()) {
            for (Stock stock : stocks) {
                if (stock.getCode().equals(symbol)) {
                    toReturn = false;
                    break;
                }
            }

        } 
        
        return toReturn;
    }
    
}

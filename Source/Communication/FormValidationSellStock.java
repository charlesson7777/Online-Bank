 /*
 * FormValidationBuyStock.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from buying stocks form
 */

 package Communication;
 
 import javax.swing.JOptionPane;
 
 import BankObjects.BankAccountSecurities;
 import BankObjects.Currency;
 import BankObjects.StockOwned;
 import BankObjects.TransactionFactory;
 import BankObjects.TransactionWithdrawal;
 import BankObjects.TypeTransaction;
 import BankObjects.User;
 
 public class FormValidationSellStock extends FormValidationUser {
    protected TransactionWithdrawal txnSender;

    public FormValidationSellStock(InstanceManager m, User user) {
        super(m, user);
    }

    @Override
    public boolean process(String[] string) {
        return false;
    }

    
    public boolean process(StockOwned stock) {
        boolean toReturn;

        try {
            
            toReturn = sellStock(stock);
            
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for number of shares.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Invalid stock or securities account.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }

        return toReturn;
    }

    private boolean sellStock(StockOwned stock) {
        boolean toReturn = false;

        if (db.sellAStockOwned(stock)) {
            String message = "Succesfully sold " + stock.getNumOfShare() + " shares of " + stock.getName() + ". " ;
            JOptionPane.showMessageDialog(null, message, "Purchased Stock", 
            JOptionPane.INFORMATION_MESSAGE);
            manager.updateStocks();
            toReturn = true;
        } else {
            JOptionPane.showMessageDialog(null, "Error in creating a new account. Please try again.", 
        "Error", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
            

        return toReturn;
    }
     
 }
 
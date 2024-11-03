 /*
 * FormValidationBuyStock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from buying stocks form
 */

package Communication;

import java.util.UUID;

import javax.swing.JOptionPane;

import BankObjects.BankAccountSecurities;
import BankObjects.Currency;
import BankObjects.Stock;
import BankObjects.StockOwned;
import BankObjects.TransactionFactory;
import BankObjects.TransactionWithdrawal;
import BankObjects.TypeTransaction;
import BankObjects.User;

public class FormValidationBuyStock extends FormValidationUser {
    protected TransactionWithdrawal txnSender;

    public FormValidationBuyStock(InstanceManager m, User user) {
        super(m, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;

        try {
            UUID stockID = UUID.fromString(response[0]);
            Long securitiesID = Long.parseLong(response[1]);
            int numShares = Integer.parseInt(response[2].trim());
            
            toReturn = buyStock(stockID, securitiesID, numShares);
            
    
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

    private boolean buyStock(UUID stockID, Long securitiesID, int numShares) {
        boolean toReturn = false;
        Stock stock = db.fetchStockById(stockID);
        BankAccountSecurities payingAccount = (BankAccountSecurities)db.getAccountById(securitiesID);
        double amount = stock.getPrice() * numShares;

        if (checkNumShares(numShares, stock)) {
            if (checkBalance(payingAccount, amount, stock.getCurrency())) {
                // create stock owned
                StockOwned stockOwned = new StockOwned(
                    UUID.randomUUID(), 
                    securitiesID, 
                    stock.getPrice(), stock.getName(), 
                    numShares, stock.getCurrency(), stockID
                );

                if (db.purchaseStock(stockOwned)) {
                    String message = "Succesfully bought " + numShares + " shares of " + stock.getName() + ". " + 
                    txnSender.getAmount() +  " " + txnSender.getCurrency() + " withdrawn";
                    JOptionPane.showMessageDialog(null, message, "Purchased Stock", 
                    JOptionPane.INFORMATION_MESSAGE);
                    manager.updateStocks();
                    toReturn = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error in creating a new account. Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }
            }
        }

        return toReturn;
    }

    private boolean checkBalance(BankAccountSecurities payingAccount, double amount, Currency stockCurrency) {
        boolean toReturn = false;
        double exchangeRateToAcc = db.getExchangeRate(stockCurrency.getId(), payingAccount.getCurrency().getId());
        double minBalance = amount * exchangeRateToAcc;
        double balance = payingAccount.getBalance();

        if (balance < minBalance) {
            JOptionPane.showMessageDialog(null, "You need at least " +  
            minBalance + " " + payingAccount.getCurrency().getName() + " in your account.", 
            "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
            
        } else {
            // try to transfer money
            double wamount = minBalance;

            txnSender = (TransactionWithdrawal)TransactionFactory.create(
                TypeTransaction.WITHDRAWAL, 
                wamount, payingAccount.getCurrency(), 
                payingAccount.getID(), payingAccount.getID(), 
                uid);

            if (db.executeTransaction(txnSender)) {
                db.addNewTransactionRecord(txnSender);
                toReturn = true;
            } else {
                JOptionPane.showMessageDialog(null, 
                "Error in withdrawal. Please try again.", 
                "Transaction Error", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            }
                
            toReturn = true;
        }

        return toReturn;
    }

    private boolean checkNumShares(int numShares, Stock stock) {
        if (stock.getNumShares() < numShares) {
            JOptionPane.showMessageDialog(null, 
            "This stock only has " + stock.getNumShares() + " shares.", "Invalid Input", 
            JOptionPane.ERROR_MESSAGE);
            return false;
        } else {

            return true;
        }

    }
    
}

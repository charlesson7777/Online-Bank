 /*
 * FormValidationDeposit.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from deposit transaction form
 */
package Communication;

import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.Currency;
import BankObjects.TransactionDeposit;
import BankObjects.TransactionFactory;
import BankObjects.TypeCurrency;
import BankObjects.TypeTransaction;
import BankObjects.User;

public class FormValidationDeposit extends FormValidationUser {
    public FormValidationDeposit(InstanceManager manager, User user) {
        super(manager, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;

        try {
            double amount = round(Double.parseDouble(response[0].trim()), 2);
            long accountNumber = Long.parseLong(response[1]);
            int currencyId = TypeCurrency.parseCurrency(response[2]).getId();
            String currencyName = response[2];

            if (initiateDeposit(amount, accountNumber, currencyId, currencyName)) {
                manager.updateBalance();
                toReturn = true;
            } else {
                toReturn = false;
            }
           
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, 
            "Please enter valid numeric values for the amount and account.", "Invalid Input", 
            JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, 
            "Invalid currency.", "Invalid Input", 
            JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
        
        return toReturn;
    }

    protected boolean initiateDeposit(double amount, long aid, int cid, String currencyName) {
        boolean toReturn;
        BankAccount account = db.getAccountById(aid);
        double exchangeRateUSD = db.getExchangeRate(TypeCurrency.USD.getId(), cid);
        Currency currency = db.getCurrencyById(cid);
        
        // check account
        if (account == null) {
            JOptionPane.showMessageDialog(null, 
            "Please enter a valid account.", "Account Not Found", 
            JOptionPane.ERROR_MESSAGE);
            toReturn = false;

            // check amount
        } else {
            Currency accountCurrency = db.getCurrencyById(account.getCurrency().getId());
            double exchangeRateAccount = db.getExchangeRate(cid, accountCurrency.getId());

            if (checkAmount(amount, exchangeRateUSD, currency.getName())) {
                // convert to account currency
                amount = amount * exchangeRateAccount;

                TransactionDeposit txn = (TransactionDeposit)TransactionFactory.create(
                    TypeTransaction.DEPOSIT, amount, currency, aid, aid, uid);

                if(db.executeTransaction(txn)) {
                    db.addNewTransactionRecord(txn);
                    String message = "Succesfully deposited " + amount + " " + accountCurrency.getName() + " to " + aid;
                    JOptionPane.showMessageDialog(null, message, 
                        "New Deposit", JOptionPane.INFORMATION_MESSAGE);
                    toReturn = true;
                } else {
                    JOptionPane.showMessageDialog(null, 
                    "Error in depositing. Please try again.", 
                    "Transaction Error", JOptionPane.ERROR_MESSAGE);
                    toReturn =  false;
                }
            } else {
                toReturn = false;
            }
        }

        return toReturn;
    }

    protected boolean checkAmount(double amount, double exchangeRate, String cname) {
        double minAmount = TypeTransaction.DEPOSIT.getMinAmount() * exchangeRate;
        if (amount < minAmount) {
            JOptionPane.showMessageDialog(null, 
            "The minimum amount for a deposit is " + minAmount + " " + cname + ".", 
            "Insufficient amount", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
    
}

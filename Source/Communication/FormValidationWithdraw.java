 /*
 * FormValidationWithdraw.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from withdrawing transactions form
 */
package Communication;

import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.Currency;
import BankObjects.TransactionFactory;
import BankObjects.TransactionWithdrawal;
import BankObjects.TypeCurrency;
import BankObjects.TypeTransaction;
import BankObjects.User;

public class FormValidationWithdraw extends FormValidationUser {
    public FormValidationWithdraw(InstanceManager manager, User user) {
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

            if (initiateWithdraw(amount, accountNumber, currencyId, currencyName)) {
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

    protected boolean initiateWithdraw(double amount, long aid, int cid, String currencyName) {
        boolean toReturn;
        BankAccount account = db.getAccountById(aid);
        // exchange rate from USD (min amount currency) to currency requested from form 
        double erMinAmount = db.getExchangeRate(TypeCurrency.USD.getId(), cid);
        Currency currReq = db.getCurrencyById(cid);
        
        // check account
        if (account == null) {
            JOptionPane.showMessageDialog(null, 
            "Please enter a valid account.", "Account Not Found", 
            JOptionPane.ERROR_MESSAGE);
            toReturn = false;

            // check amount
        } else {
            Currency accountCurrency = db.getCurrencyById(account.getCurrency().getId());

            // exchange rate from account currency to requested
            double erAccount = db.getExchangeRate(cid, accountCurrency.getId());

            if (checkAmount(amount, erMinAmount, erAccount, account.getBalance(), 
                            currReq.getName(), account.getCurrency().getName())) {

                // convert to account currency
                double wamount = amount * erAccount;
                
                // add transaction cost
                wamount *= TypeTransaction.WITHDRAWAL.getTxnCost();

                TransactionWithdrawal txn = (TransactionWithdrawal)TransactionFactory.create(
                    TypeTransaction.WITHDRAWAL, wamount, accountCurrency, aid, aid, uid);
                if(db.executeTransaction(txn)) {
                    db.addNewTransactionRecord(txn);
                    String message = "Succesfully withdrawn " + amount + " " + currReq.getName() + " from " + aid;
                    if (currReq.getId() != accountCurrency.getId()) {
                        message += " (" + wamount + " " + accountCurrency.getName() + " with transaction fee)"; 
                    }

                    JOptionPane.showMessageDialog(null, message, 
                        "New Withdraw Transaction", JOptionPane.INFORMATION_MESSAGE);
                    toReturn = true;
                } else {
                    JOptionPane.showMessageDialog(null, 
                    "Error in depositing. Please try again.", 
                    "Transaction Error", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }
            } else {
                toReturn = false;
            }
        }

        return toReturn;
    }

    protected boolean checkAmount(double amount, double erMinAmount, double erAccount, 
                double balance, String currReq, String currAcc) {

        boolean toReturn;
        double minAmount = round(TypeTransaction.WITHDRAWAL.getMinAmount() * erMinAmount, 2);
        amount = round(amount, 2);

        if (amount < minAmount) {
            JOptionPane.showMessageDialog(null, 
            "The minimum amount for a withdrawal is " + minAmount + " " + currReq + ".", 
            "Insufficient Amount", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            // convert amount desired to account currency
            double minBalance = (amount * erAccount);
            // add transaction cost
            minBalance *= TypeTransaction.WITHDRAWAL.getTxnCost();
            minBalance = round(minBalance, 2);

            if (balance < minBalance) {
                JOptionPane.showMessageDialog(null, 
                "You need " + minBalance + " " + currAcc + " in your account to complete this withdrawal.", 
                "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            } else {
                toReturn = true;
            }
        }

        return toReturn;
    }
    
}

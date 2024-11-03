 /*
 * FormValidationNewAccount.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from adding a new account form
 */
package Communication;
import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.BankAccountFactory;
import BankObjects.BankAccountSecurities;
import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.TypeTransaction;
import BankObjects.User;
import BankObjects.UserPrivilege;
import BankObjects.Currency;
import BankObjects.TransactionFactory;
import BankObjects.TransactionTransfer;
import BankObjects.TransactionWithdrawal;

public class FormValidationNewAccount extends FormValidationUser {
    TransactionTransfer txnRecord;

    public FormValidationNewAccount(InstanceManager m, User user) {
        super(m, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;

        try {
            TypeAccount type = TypeAccount.parseType(response[0]);

            if (type != TypeAccount.SECURITIES) {
                Double amount = round(Double.parseDouble(response[1].trim()), 2);
                int currencyID = TypeCurrency.parseCurrency(response[2]).getId();
                Currency currency = db.getCurrencyById(currencyID);
                toReturn = processNormal(amount, type, currencyID, currency);
            } else {
                Long aid = Long.parseLong(response[1]);
                BankAccount account = db.getAccountById(aid);
                Double amount = round(Double.parseDouble(response[2].trim()), 2);
                int currencyID = TypeCurrency.parseCurrency(response[3]).getId();
                Currency currency = db.getCurrencyById(currencyID);
                toReturn = processSecurities(account, amount, currency);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for amount.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Invalid account type or currency.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }

        return toReturn;
    }

    protected boolean processSecurities(BankAccount sender, double amount, Currency currSecurities) {
        boolean toReturn = false;
        TypeAccount type = TypeAccount.SECURITIES;

        if (checkStartingAmount(amount, type, currSecurities.getId(), currSecurities.getName())) {
            if (attempWithdraw(sender, amount, currSecurities)) {
                BankAccountSecurities newAccount = (BankAccountSecurities)
                    BankAccountFactory.create(uid, type, amount, currSecurities);
                if (db.addNewAccount(uid, newAccount)) {
                    // add the new account id
                    txnRecord.setRecipientID(newAccount.getID());

                    TransactionTransfer txnRecipient = (TransactionTransfer)
                    TransactionFactory.create(
                        TypeTransaction.TRANSFER, 
                        amount, currSecurities, 
                        sender.getID(), newAccount.getID(), 
                        newAccount.getUserID()
                    );
                    
                    db.addNewTransactionRecord(txnRecipient);
                    db.addNewTransactionRecord(txnRecord);

                    String message = "Succesfully created " + type.toString() + " account: " + newAccount.getID() + 
                    " with a starting balance of " + amount + " " + currSecurities.getName();
                    JOptionPane.showMessageDialog(null, message, "New Account Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                    manager.updateAccounts(privilege);
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

    private boolean attempWithdraw(BankAccount sender, double amount, Currency securitiesCurrency) {
        // get the exchange rate from requested amount currency to account transfer currency
        double erToTransfer = db.getExchangeRate(securitiesCurrency.getId(), sender.getCurrency().getId());
        double minBalance = round(amount * erToTransfer, 2);
        boolean toReturn;

        if (sender.getAccountType() == TypeAccount.CHECKING) {
            minBalance *= TypeTransaction.TRANSFER.getTxnCost();
        }

        double balance = sender.getBalance();

        if (balance < minBalance) {
            JOptionPane.showMessageDialog(null, "You need at least " +  
            minBalance + " " + sender.getCurrency().getName() + " in your account.", 
            "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
            
        } else if ((balance - minBalance) < UserPrivilege.MIN_VIP_AMOUNT_MAINTAIN) {
            JOptionPane.showMessageDialog(null, "Transferring this amount will cause you" + 
            "to lose your VIP benefits", 
            "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            // try to transfer money
            double wamount = minBalance;

            TransactionWithdrawal txnSender = (TransactionWithdrawal)TransactionFactory.create(
                TypeTransaction.WITHDRAWAL, 
                wamount, sender.getCurrency(), 
                sender.getID(), sender.getID(), 
                uid);

            if (db.executeTransaction(txnSender)) {
                 // record as transaction
                 txnRecord = (TransactionTransfer)
                 TransactionFactory.create(TypeTransaction.TRANSFER, 
                    wamount * -1, 
                    sender.getCurrency(), 
                    sender.getID(), sender.getID(), 
                    uid);

                 // send to db
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

    protected boolean processNormal(double amount, TypeAccount type, int currencyID, Currency currency) {
        boolean toReturn = false;
        if (checkStartingAmount(amount, type, currencyID, currency.getName())) {
            BankAccount newAccount = BankAccountFactory.create(uid, type, amount, currency);
            if (db.addNewAccount(uid, newAccount)) { 
                String message = "Succesfully created " + type.toString() + " account: " + newAccount.getID() + 
                    " with a starting balance of " + amount + " " + currency.getName();
                JOptionPane.showMessageDialog(null, message, "New Account Created", JOptionPane.INFORMATION_MESSAGE);
                manager.updateAccounts(privilege);
                toReturn = true;
            } else {
                JOptionPane.showMessageDialog(null, "Error in creating a new account. Please try again.", 
            "Error", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            }
        } 

        return toReturn;
    }

    protected boolean checkStartingAmount(double amount, TypeAccount type, int cid, String cname) {
        Double minAmount = type.getMinStartAmount(); 
        Double exchangeRate = db.getExchangeRate(TypeCurrency.USD.getId(), cid);

        if (cid == TypeCurrency.USD.getId()) {
            exchangeRate = 1.0;
        }

        minAmount *= exchangeRate;
        
        if (amount < minAmount) {
            JOptionPane.showMessageDialog(null, "The minimum deposit for opening a " + 
                type.toString().toLowerCase() + " account is " + minAmount + " " + cname, 
                "Insufficient Starting Deposit", JOptionPane.ERROR_MESSAGE);
                return false;

        } else {
            return true;
        }

    }
    
}

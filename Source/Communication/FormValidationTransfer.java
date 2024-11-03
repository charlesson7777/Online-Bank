 /*
 * FormValidationTransfer.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from transfer transaction form
 */
package Communication;

import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.TransactionDeposit;
import BankObjects.TransactionFactory;
import BankObjects.TransactionTransfer;
import BankObjects.TransactionWithdrawal;
import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.TypeTransaction;
import BankObjects.User;


public class FormValidationTransfer extends FormValidationUser {
    public FormValidationTransfer(InstanceManager manager, User user) {
        super(manager, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;
        // transferring to same account is not allowed
        try {
            if (response[1].equals(response[2].trim())) {
                JOptionPane.showMessageDialog(null, 
                "You are not allowed to transfer to the same account.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            } else {
                double amount = round(Double.parseDouble(response[0].trim()), 2);
                Long fromAccount = Long.parseLong(response[1]); 
                Long toAccount = Long.parseLong(response[2].trim()); 
                if (initiateTransfer(fromAccount, amount, toAccount)) {
                    manager.updateBalance();
                    toReturn = true;
                } else {
                    toReturn = false;
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for amount and accounts.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid currency.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
        
        return toReturn;
    }

    private boolean initiateTransfer(long senderid, double amount, long recipientID) {
        boolean toReturn;
        BankAccount sender = db.getAccountById(senderid);
        BankAccount recipient = db.getAccountById(recipientID);
        TypeTransaction type = TypeTransaction.TRANSFER;

        // invalid recipient account
        if (recipient == null) {
            JOptionPane.showMessageDialog(null, "Invalid recipient account.", 
                "Invalid Recipient", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {

            int senderCurrencyId = sender.getCurrency().getId();
            int recipientCurrencyId = recipient.getCurrency().getId();
            double erFromUSD = db.getExchangeRate(TypeCurrency.USD.getId(), senderCurrencyId);
            double erToRecipient = db.getExchangeRate(senderCurrencyId, recipientCurrencyId);

            if (checkAmount(sender, amount, erFromUSD)) {
                double wamount = amount;
                // checking account fee
                if (sender.getAccountType() == TypeAccount.CHECKING) {
                    wamount *= TypeTransaction.TRANSFER.getTxnCost();
                }
                double damount = amount * erToRecipient;
                
                wamount = round(wamount, 2);
                damount = round(damount, 2);

                TransactionWithdrawal txnSender = (TransactionWithdrawal)
                    TransactionFactory.create(TypeTransaction.WITHDRAWAL, wamount, sender.getCurrency(), senderid, recipientID, uid);
                TransactionDeposit txnRecipient = (TransactionDeposit)
                    TransactionFactory.create(TypeTransaction.DEPOSIT, damount, recipient.getCurrency(), senderid, recipientID, recipient.getUserID());
                
                    // executed as a deposit & withdrawal
                if (db.executeTransaction(txnSender) && db.executeTransaction(txnRecipient)) {
                    // record as transaction
                    TransactionTransfer txnRecordSender = (TransactionTransfer)
                        TransactionFactory.create(type, wamount * -1, sender.getCurrency(), senderid, recipientID, uid);
                    TransactionTransfer txnRecordRecipient = (TransactionTransfer)
                        TransactionFactory.create(type, damount, recipient.getCurrency(), senderid, recipientID, recipient.getUserID());

                    // send to db
                    db.addNewTransactionRecord(txnRecordSender);
                    db.addNewTransactionRecord(txnRecordRecipient);

                    String message = "Succesfully transferred " + damount + " " + recipient.getCurrency().getName() + " to " + recipient.getID() + ". \n";
                    message += wamount + " " + sender.getCurrency().getName() + " was withdrawn from " + sender.getID();
                    JOptionPane.showMessageDialog(null, message, 
                        "New Transfer", JOptionPane.INFORMATION_MESSAGE);
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

    private boolean checkAmount(BankAccount sender, double amount, double exchangeRate) {
        boolean toReturn;
        double balance = round(sender.getBalance(), 2);
        double minAmount = round(TypeTransaction.TRANSFER.getMinAmount() * exchangeRate, 2);

        if (amount < minAmount) {
            JOptionPane.showMessageDialog(null, "The minimum amount to transfer is " 
            + minAmount + " " + sender.getCurrency().getName(), 
                "Insufficient Amount", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            double minBalance = amount;

            if (sender.getAccountType() == TypeAccount.CHECKING) {
                minBalance *= TypeTransaction.TRANSFER.getTxnCost();
            }

            minBalance = round(minBalance, 2);

            if (balance < minBalance) {
                JOptionPane.showMessageDialog(null, "You need at least " +  
                    minBalance + " " + sender.getCurrency().getName() + " in your account.", 
                "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            } else {
                toReturn = true;
            }

        }

        return toReturn;
    }
    
}

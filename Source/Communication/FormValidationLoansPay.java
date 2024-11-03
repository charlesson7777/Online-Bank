 /*
 * FormValidationLoansPay.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from paying loans form
 */
package Communication;
import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.BankAccountLoan;
import BankObjects.TransactionFactory;
import BankObjects.TransactionTransfer;
import BankObjects.TransactionWithdrawal;
import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.TypeTransaction;
import BankObjects.User;

public class FormValidationLoansPay extends FormValidationUser {

    public FormValidationLoansPay(InstanceManager manager, User user) {
        super(manager, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;
        // transferring to same account is not allowed
        try {
            Long accountWithdrawID = Long.parseLong(response[0]);
            Long loanAccountID = Long.parseLong(response[1]);
            double amount = round(Double.parseDouble(response[2].trim()), 2);
            
            if (initiateLoanPayment(accountWithdrawID, loanAccountID, amount)) {
                manager.updateBalance();
                toReturn = true;
            } else {
                toReturn = false;
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

    private boolean initiateLoanPayment(long senderid, long recipientID, double amount) {
        boolean toReturn;
        BankAccount sender = db.getAccountById(senderid);
        BankAccountLoan recipient = db.getLoanById(recipientID);
        TypeTransaction type = TypeTransaction.TRANSFER;

        // invalid recipient account
        if (recipient == null) {
            JOptionPane.showMessageDialog(null, "Invalid loan account.", 
                "Invalid Account", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {

            int senderCurrencyId = sender.getCurrency().getId();
            int recipientCurrencyId = recipient.getCurrency().getId();
            double erFromUSD = db.getExchangeRate(TypeCurrency.USD.getId(), recipientCurrencyId);
            double erToSender = db.getExchangeRate(recipientCurrencyId, senderCurrencyId);

            if (checkAmount(sender, amount, erFromUSD, erToSender, recipient.getCurrency().getName(), recipient.getBalance())) {
                // convert amount to sender currency
                double wamount = amount * erToSender;
                if (sender.getAccountType() == TypeAccount.CHECKING) {
                    wamount *= TypeTransaction.TRANSFER.getTxnCost();
                }

                double damount = amount;

                TransactionWithdrawal txnSender = (TransactionWithdrawal)
                    TransactionFactory.create(TypeTransaction.WITHDRAWAL, wamount, sender.getCurrency(), senderid, recipientID, uid);
                TransactionTransfer txnRecipient = (TransactionTransfer)
                    TransactionFactory.create(TypeTransaction.TRANSFER, damount, recipient.getCurrency(), senderid, recipientID, recipient.getUserID());
                
                    // execute loan payment and withdrawal
                if (db.executeTransaction(txnSender)) {

                    if (db.payForLoan(recipientID, damount)) {
                        // record as transaction
                        TransactionTransfer txnRecord = (TransactionTransfer)
                        TransactionFactory.create(type, wamount * -1, sender.getCurrency(), senderid, recipientID, uid);

                        // send to db
                        db.addNewTransactionRecord(txnRecipient);
                        db.addNewTransactionRecord(txnRecord);

                        String message = "Succesfully transferred " + damount + " " + recipient.getCurrency().getName() + " to " + recipient.getID() + ". \n";
                        message += wamount + " " + sender.getCurrency().getName() + " was withdrawn from " + sender.getID();
                        JOptionPane.showMessageDialog(null, message, 
                            "New Loan Payment", JOptionPane.INFORMATION_MESSAGE);
                        
                        recipient = db.getLoanById(recipient.getID());
                        checkLoanBalance(recipient);
                            
                        toReturn = true;
                    }  else {
                        // return the money if failed
                        db.updateAccountBalance(senderid, wamount);
                        JOptionPane.showMessageDialog(null, 
                        "Error in depositing. Please try again.", 
                        "Transaction Error", JOptionPane.ERROR_MESSAGE);
                        toReturn = false;
                        
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, 
                    "Error in withdrawal. Please try again.", 
                    "Transaction Error", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }

            } else {
                toReturn = false;
            }
        }

        return toReturn;
    }

    private void checkLoanBalance(BankAccountLoan loanAccount) {
        if (round(loanAccount.getBalance(), 2) >= 0.0) {
            db.deleteALoanById(loanAccount.getUserID(), loanAccount.getID());
            String message = "You have succesfully paid off this loan! This account will be closed.";
            JOptionPane.showMessageDialog(null, message, 
                "Loan Completed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean checkAmount(BankAccount sender, double amount, double erFromUSD, double erToSender, String currency, double maxAmount) {
        boolean toReturn;
        double balance = round(sender.getBalance(), 2);
        double minAmount = round(TypeTransaction.TRANSFER.getMinAmount() * erFromUSD, 2);
        maxAmount = round(maxAmount, 2);

        if (amount < minAmount) {
            JOptionPane.showMessageDialog(null, "The minimum amount to transfer is " 
            + minAmount + " " + currency, 
                "Insufficient Amount", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            double minBalance = round(amount * erToSender, 2);
            if (sender.getAccountType() == TypeAccount.CHECKING) {
                minBalance *= TypeTransaction.TRANSFER.getTxnCost();
            }

            if (balance < minBalance) {
                JOptionPane.showMessageDialog(null, "You need at least " +  
                minBalance + " " + sender.getCurrency().getName() + " in your account.", 
                "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                toReturn = false;
            } else {
                if (round(maxAmount + amount, 2) > 0.0) {
                    JOptionPane.showMessageDialog(null, "You are attempting to transfer an amount greater than your loan.", 
                    "Excessive Transfer", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                } else {
                    toReturn = true;
                }
            }

        }

        return toReturn;
    }

    
    
}


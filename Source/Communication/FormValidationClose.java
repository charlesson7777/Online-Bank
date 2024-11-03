 /*
 * FormValidationClose.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from closing accounts form
 */
package Communication;

import javax.swing.JOptionPane;

import BankObjects.BankAccount;
import BankObjects.User;

public class FormValidationClose extends FormValidationUser {

    public FormValidationClose(InstanceManager m, User user) {
        super(m, user);
    }

    @Override
    public boolean process(String[] response) {
        long accountID = Long.parseLong(response[0]);
        BankAccount account = db.getAccountById(accountID);
        boolean toReturn;

        if (account == null) {
            JOptionPane.showMessageDialog(null, 
            "Account not found", "Invalid Account", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            if (round(account.getBalance(), 2) > 0) {
                // Ask for confirmation before deleting the account
                int option = JOptionPane.showConfirmDialog(null, 
                "This account has a positive balance. Are you sure you want to close it?", 
                "Confirm Account Closure", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    toReturn = deleteAccount(account);
                } else {
                    toReturn = false; 
                }

            } else {
                toReturn = deleteAccount(account);
            }
        }
        return toReturn;
    }

    private boolean deleteAccount(BankAccount account) {
        boolean toReturn;
        if (db.deleteAccount(account.getUserID(), account.getID())) {
            String message = account.getAccountType() + " - " + String.valueOf(account.getID()).substring(12) + 
             " has been closed.";
            JOptionPane.showMessageDialog(null, 
            message, "Account Closed", JOptionPane.INFORMATION_MESSAGE);
            manager.updateAccounts(privilege);
            toReturn = true;
        } else {
            JOptionPane.showMessageDialog(null, 
            "Error deleting. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }

        return toReturn;
    }
    
}

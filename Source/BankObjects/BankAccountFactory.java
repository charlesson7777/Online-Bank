 /*
 * BankAccountFactory.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Factory method to make bank accounts
 */

package BankObjects;
import java.util.UUID;

public class BankAccountFactory {
    
    
    // creates a brand new account
    public static BankAccount create(UUID userID, TypeAccount type, double startingAmount, Currency currency) {
        BankAccount newAccount;
        BANCreator accountIDgenerator = new BANCreator();

        switch(type) {
            case SAVING:
                newAccount = new BankAccountSaving(userID);
                break;
            case CHECKING:
                newAccount = new BankAccountChecking(userID);
                break;
            case LOAN:
                newAccount = new BankAccountLoan(userID);
                startingAmount *= -1;
                break;
            case SECURITIES:
                newAccount = new BankAccountSecurities(userID);
                break;
            default:
                throw new IllegalArgumentException("Unknown bank type");
        }

        long id = accountIDgenerator.getNewIdentifier();
        newAccount.setAccountNumber(id);
        newAccount.setBalance(startingAmount);
        newAccount.setCurrency(currency);

        return newAccount;
    }
}

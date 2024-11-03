 /*
 * BankAccountSecurities.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a securities bank account
 */
package BankObjects;
import java.util.UUID;

public class BankAccountSecurities extends BankAccount {
    // in dollars 
    public static final double MINIMUM_DEPOSIT = 1000.00;
    
    public BankAccountSecurities(UUID userID) {
        super(userID);
        this.type = TypeAccount.SECURITIES;
    }

}

 /*
 * BankAccountChecking.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * has all attributes to represent a Checking Accounts
 * 
 */
package BankObjects;
import java.util.UUID;

public class BankAccountChecking extends BankAccount {
    protected double limit;

    public BankAccountChecking(UUID userID) {
        super(userID);
        this.type = TypeAccount.CHECKING;
    }

    
    

}

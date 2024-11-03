 /*
 * BankAccountSaving.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a savings bank account
 */
package BankObjects;
import java.util.UUID;

public class BankAccountSaving extends BankAccount {
    
    public BankAccountSaving(UUID userID) {
        super(userID);
        this.type = TypeAccount.SAVING;
    }


}

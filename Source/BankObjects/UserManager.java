 /*
 * UserManager.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a bank manager
 */
package BankObjects;
import java.util.UUID;

public class UserManager extends User {
    
    public UserManager(UUID uid) {
        super(uid);
        privilege = UserPrivilege.MANAGER;
    }
}

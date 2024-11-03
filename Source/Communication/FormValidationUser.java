 /*
 * FormValidationUser.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Generic class that encapsulates forms that has to do 
 * with user related activities. Thus, it contains user
 * information such as uid and privilege
 */
package Communication;

import java.util.UUID;

import BankObjects.User;
import BankObjects.UserPrivilege;

public abstract class FormValidationUser extends FormValidation {
    protected UUID uid;
    protected UserPrivilege privilege;

    public FormValidationUser(InstanceManager m, User user) {
        super(m);
        uid = user.getID();
        privilege = user.getPrivilege();
    }

    protected UUID getUserId() {
        return uid;
    }

    protected UserPrivilege getPrivilege() {
        return privilege;
    }
    
}

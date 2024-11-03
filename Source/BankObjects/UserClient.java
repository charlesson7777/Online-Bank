 /*
 * Stock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a client of the bank
 */
package BankObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserClient extends User {
    protected String billingAddress;
    // list of account number
    protected List<Long> accountsList;
    // in dollars
    protected final double NEW_ACCOUNT_FEE = 25;
    
    // create a new user 
    public UserClient(String first, String last, String un, String pw) {
        super(UUID.randomUUID());
        setFirstName(first);
        setLastName(last);
        setUsername(un);
        setPassword(pw);
        setPhoneNumber("");
        setEmail("");
        setAddress("");
        super.setPrivilege(UserPrivilege.NORMAL);
    }
    
    public UserClient(UUID uid) {
        super(uid);
        privilege = UserPrivilege.NORMAL;
        accountsList = new ArrayList<>();
    }

    public void setBillingAddress(String address) {
        billingAddress = address;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setUID(UUID userID) {
        uid = userID;
    }

    public UUID getUID() {
        return uid;
    }


}
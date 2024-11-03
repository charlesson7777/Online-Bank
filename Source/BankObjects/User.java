 /*
 * User.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a generic user of the Online Bank
 * 
 */
package BankObjects;
import java.util.UUID;

public abstract class User {
    protected String firstName;
    protected String lastName; 
    protected String username;
    protected String phonenumber;
    protected String email;
    protected String address;
    protected String password;
    protected UUID uid;
    protected UserPrivilege privilege;

    public User(UUID uid) {
        this.uid = uid;
    }

    public void setUsername(String u) {
        username = u;
    }

    public String getUsername() {
        return username;
    }

    public void setPhoneNumber(String num) {
        phonenumber = num;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPassword(String s) {
        password = s;
    }

    public String getPassword() {
        return password;
    }

    public UUID getID() {
        return uid;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }
    
    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setFirstName(String n) {
        firstName = n;
    }

    public void setLastName(String n) {
        lastName = n;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
 /*
 * UserPrivilege.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * enumerates different types of users and contains information 
 * for client upgrades 
 */
package BankObjects;
public enum UserPrivilege {
    NORMAL, 
    VIP, 
    MANAGER;

    // IN DOLLARS
    public static final double MIN_VIP_AMOUNT_CREATION = 5000.0;
    public static final double MIN_VIP_AMOUNT_MAINTAIN = 2500.0;
}

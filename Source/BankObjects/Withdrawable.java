 /*
 * Withdrawable.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * interface to group bank accounts that you can withdraw from
 */
package BankObjects;
public interface Withdrawable {
    public abstract boolean withdraw(double amount);
    public abstract boolean transfer(BankAccount account, double amount);
}

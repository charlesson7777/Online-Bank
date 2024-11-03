 /*
 * UserClientVIP.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a VIP client of the bank 
 * These are clients that are able to trade stocks.
 */
package BankObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserClientVIP extends UserClient {
    public static final double MIN_BALANCE = 2500.00;
    private List<StockOwned> stocks;

    public UserClientVIP(UUID uid) {
        super(uid);
        privilege = UserPrivilege.VIP;
        stocks = new ArrayList<>();
    }
}

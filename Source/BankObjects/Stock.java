 /*
 * Stock.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a stock
 */

package BankObjects;
import java.util.UUID;

public class Stock {
    protected String name;
    protected UUID sid;
    protected int numShares;
    protected double price;
    protected Currency currency;
    protected String code;

    public Stock(String name, int numShares, double price, Currency currency) {
        this.name = name;
        this.numShares = numShares;
        this.price = price;
        this.currency = currency;
    }

    public void setSid(UUID sid) {
        this.sid = sid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public UUID getID() {
        return sid;
    }

    public int getNumShares() {
        return numShares;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public double getPrice(int numShares) {
        return price*numShares;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean buyShares(int i) {
        boolean toReturn;
        if (numShares > i) {
            numShares -= i;
            toReturn = true;
        } else {
            toReturn = false;
        }

        return toReturn;
    }

    
}

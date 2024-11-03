 /*
 * Currency.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a Currency
 */
package BankObjects;

public class Currency {

    private final int id;
    private final String sign;
    private final String name;

    public Currency(int id, String sign, String name) {
        this.id = id;
        this.sign = sign;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getSign() {
        return sign;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
    

}

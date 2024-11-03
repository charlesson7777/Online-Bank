 /*
 * Collateral.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a Collateral, used for Loans
 */
package BankObjects;

import java.util.UUID;

public class Collateral {
    private UUID idcollateral;
    private UUID uid;
    private TypeCollateral type;
    private double value;
    private Currency currency;

    public Collateral(UUID uid, TypeCollateral type, double value, Currency currency) {
        this.type = type;
        this.uid = uid;
        this.value = value;
        this.currency = currency;
    }

    public void setIdCollateral(UUID idcollateral) {
        this.idcollateral = idcollateral;
    }

    public UUID getIdCollateral() {
        return idcollateral;
    }

    public UUID getUserId() {
        return uid;
    }

    public void setType(TypeCollateral t) {
        type = t;
    }

    public TypeCollateral getType() {
        return type;
    }

    public void setValue(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("negative value");
        }
        value = amount;
    }

    public void setCurrency(Currency c) {
        currency = c;
    }

    public UUID getID() {
        return uid;
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }
    
}

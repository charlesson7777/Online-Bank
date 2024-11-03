 /*
 * TypeCollateral.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Enumerates types of collaterals supported. 
 */
package BankObjects;
public enum TypeCollateral {
    PROPERTY, 
    VEHICLE;

    public static TypeCollateral parseCollateral(String str) {
        switch(str.toUpperCase()) {
            case "PROPERTY":
                return PROPERTY;
            case "VEHICLE":
                return VEHICLE;
            default:
                throw new IllegalArgumentException("Unknown TypeCollateral: " + str);
        }
    }

}

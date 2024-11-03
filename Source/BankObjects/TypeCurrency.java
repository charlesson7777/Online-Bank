 /*
 * Stock.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * enumerates currencies supported
 */
package BankObjects;

public enum TypeCurrency {
    USD(1),
    EURO(2),
    YEN(3);

    private final int id;

    private TypeCurrency(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static String[] getCurrencyStrArr() {
        String[] toReturn = new String[TypeCurrency.values().length];
        int i = 0;
        for (TypeCurrency currency : TypeCurrency.values()) {
            toReturn[i++]  = currency.toString();
        }
        return toReturn;
    }

    public String toString() {
        return name();
    }

    public static TypeCurrency parseCurrency(String string) {
        TypeCurrency toReturn;
        switch(string.toUpperCase()) {
            case "USD": 
                toReturn = USD;
                break;
            case "EURO": 
                toReturn = EURO;
                break;
            case "YEN":
                toReturn = YEN;
                break;
            default: 
                throw new IllegalArgumentException("unimplemented currency");
        }

        return toReturn;
    }
    
}


 /*
 * FormValidation.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Follows the strategy pattern to handle all processing 
 * that has to do with forms
 */
package Communication;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class FormValidation {
    protected DB db;
    protected InstanceManager manager;

    public FormValidation(InstanceManager m) {
        manager = m;
        db = DB.getDB();
    }

    public abstract boolean process(String[] response);

    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    protected double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}

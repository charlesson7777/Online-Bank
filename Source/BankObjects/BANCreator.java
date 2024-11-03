 /*
 * BANCreator.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Generates unique bank account numbers
 */

package BankObjects;
import java.util.Random;

import Communication.DB;

public class BANCreator {
    private final String BANK_IDENTIFIER = "362051";
    private final String CHECK_SUM = "7";
    private static final int LENGTH = 16;


    public BANCreator() {
    }

    public long getNewIdentifier() {
        String out = BANK_IDENTIFIER; 
        DB db = DB.getDB();

        Random random = new Random();

        // unique user identifier is is length 9
        int min = 100000000; 
        int max = 999999999;
        long newAccountNum;

        do {
            int userIdentifier = random.nextInt(max - min + 1) + min;
            out += userIdentifier;
            out += CHECK_SUM;
            newAccountNum = Long.parseLong(out);

            // check with the database to make sure it is unique
        } while (db.getAccountById(newAccountNum) != null);
        

        return newAccountNum;
    }

    public static String toString(long accountNumber) {
        String accountNumberStr = Long.toString(accountNumber);
        String out = "";
    
        for (int i = 0; i < LENGTH; i++) {
            out += accountNumberStr.charAt(i);
            if ((i + 1) % 4 == 0 && i != LENGTH - 1) {
                out += " ";
            }
        }
    
        return out;
    }
    


}

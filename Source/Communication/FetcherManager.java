 /*
 * FetcherManager.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Fetcher for manager
 */
package Communication;

import java.util.UUID;

public abstract class FetcherManager {
    protected DBManager db;

    public FetcherManager() {
        db = DBManager.getDBManager();
    }
    
    public abstract void fetch(UUID uid);
}

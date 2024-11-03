 /*
 * Fetcher.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Follows the strategy pattern to fetch and contain useful data
 * from the database 
 */
package Communication;

import java.util.UUID;

public abstract class FetcherClient implements Fetcher {
    protected DB db;

    public FetcherClient() {
        db = DB.getDB();
    }
    
    public abstract void fetch(UUID uid);
}

 /*
 * Stock.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * enumerates types of transactions supported
 */
package BankObjects;

public enum TypeTransaction {
    DEPOSIT(5, 1.00), 
    WITHDRAWAL(10, 1.01),
    TRANSFER(0, 1.01);
    
    private final int minAmount;
    private final double txnCost;

    private TypeTransaction(int minAmount, double txnCost) {
        this.minAmount = minAmount;
        this.txnCost = txnCost;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public double getTxnCost() {
        return txnCost;
    }
}

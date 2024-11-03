 /*
 * TypeAccount.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * enumerates the different types of accounts and stores 
 * useful information about these accounts, such as the 
 * minimum starting amount (in USD) for ease of access
 */
package BankObjects;
public enum TypeAccount {
    SAVING(500.0, true),
    CHECKING(20.0, true),
    LOAN(1000.0, false),
    SECURITIES(1000.0, true);

    private final double minStartAmount;
    private final boolean withdrawalable;

    private TypeAccount(double minStartAmount, boolean withdrawalable) {
        this.minStartAmount = minStartAmount;
        this.withdrawalable = withdrawalable;
    }

    public String toString() {
        return name();
    }

    public static String[] getAccountStrArr(UserPrivilege privilege) {
        String[] toReturn;

        switch (privilege) {
            case NORMAL:
                toReturn = new String[] {SAVING.toString(), CHECKING.toString()};
                break;

            case VIP:
                toReturn = new String[] {SAVING.toString(), CHECKING.toString(), SECURITIES.toString()};
                break;

            default:
                throw new IllegalArgumentException("This account cannot create a new account");
        }
        
        return toReturn;
    }

    public static TypeAccount parseType(String type) {
        for (TypeAccount account : TypeAccount.values()) {
            if (account.toString().equalsIgnoreCase(type)) {
                return account;
            }
        }
        throw new IllegalArgumentException("Unimplemented account type: " + type);
    }

    public double getMinStartAmount() {
        return minStartAmount;
    }

    public boolean isWithdrawable() {
        return withdrawalable;
    }
}

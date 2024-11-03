 /*
 * BankAccountLoan.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Has all attributes to represent a loans bank account
 */
package BankObjects;
import java.sql.Date;
import java.util.UUID;


public class BankAccountLoan extends BankAccount {
    private Collateral collateral;
    private Date date;
    // collateral coverage ratio 
    private static final double COLLATERAL_RATIO_NORMAL = 1.20;
    private static final double COLLATERAL_RATIO_VIP = 1.10;

    public BankAccountLoan(Collateral collateral, UUID userID) {
        super(userID);
        type = TypeAccount.LOAN;
        setCollateral(collateral);
        this.date = new Date(System.currentTimeMillis());
    }
    public BankAccountLoan(UUID userID) {
        this(null, userID);
    }

    public static double getCollateralCoverageRatio(UserPrivilege privilege) {
        double ratio;

        switch(privilege){
            case NORMAL:
                ratio = COLLATERAL_RATIO_NORMAL;
                break;
            case VIP:
                ratio = COLLATERAL_RATIO_VIP;
                break;
            default:
                ratio = COLLATERAL_RATIO_NORMAL;
                break;
        }

        return ratio;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setCollateral(Collateral collateral) {
        this.collateral = collateral;
    }

    public Collateral getCollateral() {
        return collateral;
    }

}

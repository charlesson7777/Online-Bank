 /*
 * FormValidationLoansReq.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from request loans form
 */
package Communication;

import java.util.UUID;

import javax.swing.JOptionPane;

import BankObjects.BankAccountFactory;
import BankObjects.BankAccountLoan;
import BankObjects.Collateral;
import BankObjects.Currency;
import BankObjects.TypeAccount;
import BankObjects.TypeCollateral;
import BankObjects.TypeCurrency;
import BankObjects.User;

public class FormValidationLoansReq extends FormValidationNewAccount {
    public FormValidationLoansReq(InstanceManager manager, User user) {
        super(manager, user);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;
        try {

            double amount = round(Double.parseDouble(response[0].trim()), 2);
            int loanCurrencyID = TypeCurrency.parseCurrency(response[1].trim()).getId();
            TypeCollateral collateralType = TypeCollateral.parseCollateral(response[2].trim());
            double collateralValue = Double.parseDouble(response[3].trim());
            int collateralCurrencyID = TypeCurrency.parseCurrency(response[4].trim()).getId();

            if (requestLoanAccount(amount, loanCurrencyID, collateralType, collateralValue, collateralCurrencyID)) {
                manager.updateAccounts(privilege);
                toReturn = true;
            } else {

                toReturn = false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric input for amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Invalid types", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
        
        return toReturn;
    }

    private boolean requestLoanAccount(double amountRequested, int loanCurrencyID, TypeCollateral collateralType, double collateralValue, int collateralCurrencyID) {
        boolean toReturn;
        Currency collateralCurrency = db.getCurrencyById(collateralCurrencyID);
        Currency loanCurrency = db.getCurrencyById(loanCurrencyID);
        double exchangeRateToLoan = db.getExchangeRate(collateralCurrencyID, loanCurrencyID);

        if (checkStartingAmount(amountRequested, TypeAccount.LOAN, loanCurrencyID, loanCurrency.getName())) {
            if (checkCollateral(amountRequested, collateralValue, exchangeRateToLoan, loanCurrency.getName())) {
                // register collateral 
                Collateral collateral = new Collateral(UUID.randomUUID(), collateralType, collateralValue, collateralCurrency);
                db.addCollateralToDB(collateral);
                
                BankAccountLoan loanAccount = (BankAccountLoan) BankAccountFactory.create(uid, TypeAccount.LOAN, amountRequested, loanCurrency);
                loanAccount.setCollateral(collateral);
                
                if (db.addALoanToDB(loanAccount)) {
                    String message = "Succesfully created " + TypeAccount.LOAN.toString() + " account: " + loanAccount.getID() + 
                    " with a starting balance of " + amountRequested + " " + loanCurrency.getName();
                    JOptionPane.showMessageDialog(null, message, "New Account Created", JOptionPane.INFORMATION_MESSAGE);
                    toReturn = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error in creating a new account. Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }

            } else {

                toReturn = false;
            }

        } else {

            toReturn = false;
        }

        return toReturn;
    }

    private boolean checkCollateral(double amountRequested, double collateralValue, double exchangeRate, String cname) {
        boolean toReturn;
        double minValue = round(BankAccountLoan.getCollateralCoverageRatio(privilege) * amountRequested, 2);
        double realCollateralValue = round(collateralValue * exchangeRate, 2);
        if (realCollateralValue < minValue) {
            JOptionPane.showMessageDialog(null, "This loan requires a collateral of at least " + minValue + " " + cname, 
            "Insufficient Value", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } else {
            toReturn = true;
        }

        return toReturn;
    }
    
}

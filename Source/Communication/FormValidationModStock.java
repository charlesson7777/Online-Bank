 /*
 * FormValidationModStock.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from manager editing stocks form
 */
package Communication;

import java.util.UUID;

import javax.swing.JOptionPane;


public class FormValidationModStock extends FormValidationManager {

    public FormValidationModStock(InstanceManager manager) {
        super(manager);
    }

    @Override
    public boolean process(String[] response) {
        boolean toReturn;
        try {
            String stockSymbol = response[0].trim();
            double pricePerStock = round(Double.parseDouble(response[1].trim()), 2);
            UUID sid = UUID.fromString(response[2]);

            if (validateStock(stockSymbol, pricePerStock)) {
                if (managerDB.updateStockPrice(sid, pricePerStock)) {
                    JOptionPane.showMessageDialog(null, "Succefully updated stock price.", 
                "Stock Price Update", JOptionPane.INFORMATION_MESSAGE);
                    toReturn = true;

                } else {
                    JOptionPane.showMessageDialog(null, "Error. Please try again", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                    toReturn = false;
                }
            } else {
             
                toReturn = false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for price per stock and number of shares.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid currency.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        }
        
        return toReturn;
    }

    private boolean validateStock(String stockSymbol, double pricePerStock) {
        boolean toReturn = true;

        if (pricePerStock <= 0) {
            JOptionPane.showMessageDialog(null, "Price per stock should be a positive value.", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            toReturn = false;
        } 
        
        return toReturn;
    }
    
}

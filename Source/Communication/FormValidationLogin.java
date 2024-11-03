 /*
 * FormValidationLogin.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from login form
 */
    
package Communication;
import javax.swing.JOptionPane;
import BankObjects.User;

    public class FormValidationLogin extends FormValidation {

        public FormValidationLogin(InstanceManager manager) {
            super(manager);
        }

        @Override
        public boolean process(String[] response) {
            String username = response[0];
            String password = response[1];
            User user = db.loginValidation(username, password);
            
            if (user == null) {
                JOptionPane.showMessageDialog(null, 
                "Incorrect username or password", "Incorrect Login", JOptionPane.ERROR_MESSAGE);
            } else {
                User updatedUser = manager.checkUserPrivilege(user);
                manager.onLoginSuccess(updatedUser);
            }

            return true;
        }

        
    }

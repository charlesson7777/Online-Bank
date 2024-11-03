 /*
 * FormValidationRegistration.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Processes responses from account registration form
 */
package Communication;

import javax.swing.JOptionPane;
import BankObjects.UserClient;

public class FormValidationRegistration extends FormValidation {
    private final int MIN_PASSWORD_LENGTH = 9;

    public FormValidationRegistration(InstanceManager manager) {
            super(manager);
        }

        @Override
        public boolean process(String[] response) {
            String firstname = response[0];
            String lastname = response[1];
            String username = response[2].toLowerCase();
            String password = response[3];
            String passwordReconfirm = response[4];

            if (username.equals("")) {
                JOptionPane.showMessageDialog(null, 
                "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (firstname.trim().equals("") || lastname.trim().equals("")) {
                JOptionPane.showMessageDialog(null, 
                "First name and last name cannot be empty",
                     "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (password.length() == 0 || passwordReconfirm.length() == 0) {
                JOptionPane.showMessageDialog(null, 
                "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return false; 
            }
    
            if (!password.equals(passwordReconfirm)) {
                JOptionPane.showMessageDialog(null, 
                "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return false; 
            }
    
            if (password.length() < MIN_PASSWORD_LENGTH) {
                JOptionPane.showMessageDialog(null, 
                "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long", 
                "Error", JOptionPane.ERROR_MESSAGE);
                return false; 
            }

            if (!firstname.isEmpty()) {
                firstname = firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase();
            }
            if (!lastname.isEmpty()) {
                lastname = lastname.substring(0, 1).toUpperCase() + lastname.substring(1).toLowerCase();
            }

            UserClient user = new UserClient(firstname, lastname, username, password); 
            boolean newUserRegistered = db.addNewUser(user);
            
            if (newUserRegistered == false) {
                JOptionPane.showMessageDialog(null, 
                "This username is taken. Please try another one.", "Registration failed", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                JOptionPane.showMessageDialog(null, 
                "Registration success!", "Registration success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
    
}

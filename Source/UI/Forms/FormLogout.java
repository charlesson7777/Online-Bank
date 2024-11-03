 /*
 * FormLogout.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for loging out
 */
package UI.Forms;

import java.awt.Dimension;

import javax.swing.SwingConstants;

import Communication.InstanceManager;
public class FormLogout extends Form {

    public FormLogout(InstanceManager m) {
        super("Logout", m);
        initialize();
    }

    private void initialize() {
        int width = (int)(screenSize.width * 0.35);
        int height = (int) (screenSize.height * 0.30);
        setSize(new Dimension(width, height));
        setTitle("Are you sure you want to logout?");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setText("Yes");
        submitButton.addActionListener(e -> process());
    }


    private void process() {
        manager.logout();
        this.dispose();
    }
    
}

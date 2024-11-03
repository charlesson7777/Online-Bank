 /*
 * NoButton.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Used to get a minimalistic scroll bar theme
 */

package UI.Theme;
import java.awt.Dimension;
import javax.swing.JButton;

public class NoButton extends JButton {
    @Override
    public Dimension getMaximumSize() 
    {          
        return new Dimension();
    }
}
 /*
 * MinmalTextField.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Setup a unified theme and structure for all textfields
 */
package UI.Theme;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MinimalTextField extends JTextField {

    private Color underlineColor = Pallete.MEDIUM_GREY;

    public MinimalTextField() {
        super();
        setOpaque(false);
        setBorder(null);
    }

    public void setUnderlineColor(Color color) {
        this.underlineColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int underlineY = getHeight() - 1;
        g2d.setColor(underlineColor);
        g2d.drawLine(0, underlineY, getWidth(), underlineY);

        g2d.dispose();
    }
}

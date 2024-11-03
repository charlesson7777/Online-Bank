 /*
 * RoundedButton.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Set up a unified theme for all buttons
 */
package UI.Theme;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    protected Color myColor;
    protected Color hoverColor;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setColor(getBackground());
        setHoverColor(Color.WHITE);
    }

    public void setHoverColor(Color color) {
        hoverColor = color;
    }

    public void setColor(Color c) {
        myColor = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(hoverColor.brighter());
        } else if (getModel().isRollover()) {
            g.setColor(hoverColor); 
        } else {
            g.setColor(myColor);
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }

}


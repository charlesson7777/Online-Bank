 /*
 * RoundedPanel.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Set up a unified theme for rounded panels
 */
package UI.Theme;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private int arc;
    private Color color;

    public RoundedPanel(int arc, Color color) {
        this.arc = arc;
        setOpaque(false);
        this.color = color;
    }

    public RoundedPanel() {
        this(20, Pallete.PALE_BLUE); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, width, height, arc, arc);
        g2d.dispose();
    }
}


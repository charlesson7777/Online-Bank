 /*
 * RoundedToggleButton.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Set up a unified theme for "toggle buttons" used to switch
 * modes for certain forms
 */
package UI.Theme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;

public class RoundedToggleButton extends JToggleButton {
    private Color backgroundColor;
    private Color selectedColor;
    private static final int ARC = 20;

    public RoundedToggleButton(String text) {
        super(text);
        setUI(new RoundedButtonUI());
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setSelected(false);
    }

    public void setBackground(Color c) {
        backgroundColor = c;
    }

    public void setSelected(Color c) {
        selectedColor = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isSelected()) {
            g.setColor(selectedColor);
        } else {
            g.setColor(backgroundColor);
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Do not paint border
    }

    private class RoundedButtonUI extends BasicButtonUI {
        @Override
        protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
            if (b.getModel().isPressed()) {
                textRect.x += 1;
                textRect.y += 1;
            }
            super.paintText(g, b, textRect, text);
        }

    }
}

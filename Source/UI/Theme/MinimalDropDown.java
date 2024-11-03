 /*
 * MinimalDropDown.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Setup a minimalistic theme for all dropdowns
 */
package UI.Theme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class MinimalDropDown<T> extends JComboBox<T> {

    public MinimalDropDown(T[] items) {
        super(items);
        setFont(TextStyle.BODY_FONT);
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                button.setIcon(new ArrowIcon());
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
                return button;
            }
        });
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Pallete.MEDIUM_GREY));
        setBackground(Color.WHITE);
    }

    private static class ArrowIcon implements Icon {
        private final int width = 8;
        private final int height = 8;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Pallete.MEDIUM_GREY);
            int[] xp = {x, x + width, x + width / 2};
            int[] yp = {y, y, y + height};
            g2.fillPolygon(xp, yp, 3);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }
}



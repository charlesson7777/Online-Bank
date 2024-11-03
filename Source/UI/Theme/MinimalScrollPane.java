 /*
 * MinimalScrollPane.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Setup a unified theme and structure for all scroll panes
 */

package UI.Theme;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class MinimalScrollPane extends JScrollPane {
    public MinimalScrollPane(JComponent component, Color trackColor, Color thumbColor) {
        super(component);
        getViewport().setBackground(Color.WHITE);

        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
        setViewportBorder(BorderFactory.createEmptyBorder());
        getVerticalScrollBar().setUnitIncrement(15); 

        getVerticalScrollBar().setUI(new MinimalScrollBarUI(trackColor, thumbColor));
        getHorizontalScrollBar().setUI(new MinimalScrollBarUI(trackColor, thumbColor));
    }

    private static class MinimalScrollBarUI extends BasicScrollBarUI {
        protected Color trackColor;
        protected Color thumbColor;

        public MinimalScrollBarUI(Color track, Color thumb) {
            trackColor = track;
            thumbColor = thumb;
        }

        protected JButton createIncreaseButton(int i) {
            JButton button = new NoButton();
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder());
            return button;
        }
        protected JButton createDecreaseButton(int i){
            JButton button = new NoButton();
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder());
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            int radius = 13;
            int width = (int)(r.width / 2);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(thumbColor);
            g2.fillRoundRect(r.x,r.y,width,r.height,radius,radius);
            g2.setPaint(thumbColor);
            g2.drawRoundRect(r.x,r.y,width,r.height,radius,radius);
            g2.dispose();
        }
        

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }
    }
}

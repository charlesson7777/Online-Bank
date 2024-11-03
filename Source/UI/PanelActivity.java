 /*
 * PanelActivity.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * The main activity part of the online bank
 */
package UI;
import javax.swing.*;

import UI.Theme.TextStyle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Color;


public class PanelActivity extends JPanel {
    protected JPanel titleContainer;
    protected JLabel title;
    protected JPanel activity;
    protected Dimension screenSize;
    protected int marginX;
    protected int marginY;
    private final double ACTIVITY_RATIO = 0.8;

    public PanelActivity(String activityName) {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        marginX = (int) (screenSize.width / ((10.0 * 8) / 4));
        marginY = (int) (screenSize.height / ((10.0 * 8) / 4));
    
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        title = new JLabel(activityName);
        title.setFont(TextStyle.HEADING_FONT);
        title.setForeground(Color.BLACK);

        titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleContainer.setBackground(Color.WHITE);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(marginY, marginX, marginY/2, 0));
        titleContainer.add(title);
        add(titleContainer, BorderLayout.NORTH);
        
        activity = new JPanel();
        activity.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, marginX));
        activity.setBackground(Color.WHITE);
        add(activity, BorderLayout.CENTER); 
        
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
    }

    public int getActivityWidth() {
        int width = (int) (screenSize.width * ACTIVITY_RATIO);
        return width;
    }
    

    public PanelActivity() {
        this("Title");
    }

    public void setTitleContainer(JPanel panel) {
        titleContainer = panel;
    }

    public Dimension getActivitySize() {
        int width = activity.getWidth() - activity.getInsets().left - activity.getInsets().right;
        int height = activity.getHeight() - activity.getInsets().top - activity.getInsets().bottom;
        return new Dimension(width, height);
    }
    

    public void setTitle(String name) {
        title.setText(name);
    }

    public void setActivity(JPanel panel) {
        activity = panel;
    }

    public JPanel getTitleContainer() {
        return titleContainer;
    }

    public JLabel getTitle() {
        return title;
    }

    public JPanel getActivity() {
        return activity;
    }
    
}

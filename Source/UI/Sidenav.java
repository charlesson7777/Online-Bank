 /*
 * Sidenav.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Generic sidenav structure used in the bank
 */
package UI;
import javax.swing.*;

import UI.Forms.Form;
import UI.Forms.FormLogout;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.TextStyle;

import java.awt.*;

public class Sidenav extends JPanel {
    protected JPanel navigationPanel;
    protected int marginX;
    protected int marginY;
    protected static Color backgroundColor = Pallete.PALE_BLUE;
    protected Dimension screenSize;
    protected RoundedButton accountName;
    protected int navButtonMargins;
    protected OnlineBank mainFrame;
    
    public Sidenav(OnlineBank mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(backgroundColor);
        setLayout(new BorderLayout());

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        marginX = screenSize.width / ((10 * 8) / 2);
        marginY = screenSize.height / ((int)(5.5 * 8) / 2);
        navButtonMargins = screenSize.height / (int)((5.5 * 8) / 0.25);
        setup();
    }

    protected void setup() {
        // Bank name
        JLabel bankName = new JLabel(MyStrings.BANK_NAME);
        bankName.setForeground(Pallete.ORANGE);
        bankName.setFont(TextStyle.HEADING_FONT);
        bankName.setHorizontalAlignment(SwingConstants.LEFT);
        bankName.setBorder(BorderFactory.createEmptyBorder(0, 0, marginX, 0)); 
        add(bankName, BorderLayout.NORTH);

        // default name
        accountName = new RoundedButton("Talia Tondang");
        accountName.setForeground(Pallete.LIGHT_GREY);
        accountName.setFont(TextStyle.BODY_FONT);
        accountName.setHorizontalAlignment(SwingConstants.LEFT);
        accountName.setColor(Color.WHITE);
        accountName.setHoverColor(Color.WHITE);
        accountName.setBorder(BorderFactory.createEmptyBorder(marginX/2, marginY/2, marginX/2, 0)); 
        accountName.addActionListener(e -> openForm(new FormLogout(mainFrame.getCommManager())));
        add(accountName, BorderLayout.SOUTH);

        // Navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setOpaque(false);
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        add(navigationPanel, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createEmptyBorder(marginX, marginY, marginX, marginY));
    }

    public void setAccountName(String firstlast) {
        accountName.setText(firstlast);
    }

    public void setActive(JButton button, boolean active) {
        ((RoundedButton)button).setColor(!active ? Pallete.PALE_BLUE : Color.WHITE);
        button.setFont(active ? TextStyle.BODY_BOLD : TextStyle.BODY_FONT);
        button.setForeground(active ? Pallete.DARK_BLUE : Pallete.LIGHT_GREY);
        button.setBackground(active ? Color.WHITE : backgroundColor);
    }

    protected JButton createNavElement(String text, boolean active) {
        int buttonHeight = screenSize.height / (int)((5.5 * 8) / 2);

        RoundedButton button = new RoundedButton(text);
        button.setColor(Pallete.PALE_BLUE);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // active vs unactive
        button.setFont(active ? TextStyle.BODY_BOLD : TextStyle.BODY_FONT);
        button.setForeground(active ? Pallete.DARK_BLUE : Pallete.LIGHT_GREY);
        button.setBackground(active ? Color.WHITE : backgroundColor);
        

        return button;
    }

    protected void openForm(Form formPanel) {
        formPanel.setLocationRelativeTo(null); 
        formPanel.setVisible(true);
    }
}

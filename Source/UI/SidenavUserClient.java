 /*
 * SidenavUserClient.java
 * 
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Specific sidenav for normal clients
 */
package UI;
import java.awt.Dimension;

import javax.swing.JButton;

import javax.swing.Box;

public class SidenavUserClient extends Sidenav {
    protected JButton homeNav;
    protected JButton accountsNav;
    protected JButton transactionsNav;

    public SidenavUserClient(OnlineBank mainFrame) {
        super(mainFrame);
        homeNav = createNavElement("Home", false);
        accountsNav = createNavElement("Accounts", false);
        transactionsNav = createNavElement("Transactions", false);
        navigationPanel.add(homeNav);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(accountsNav);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(transactionsNav);
    }

    public void initialize() {
        homeNav.addActionListener(e -> mainFrame.switchPanel("home")); 
        accountsNav.addActionListener(e -> mainFrame.switchPanel("accounts")); 
        transactionsNav.addActionListener(e -> mainFrame.switchPanel("transactions")); 
    }

    public JButton getHomeNav() {
        return homeNav;
    }

    public JButton getAccountsNav() {
        return accountsNav;
    }

    public JButton getTransactionsNav() {
        return transactionsNav;
    }

    // sets them all to false
    public void resetNavButtons() {
        setActive(homeNav, false);
        setActive(accountsNav, false);
        setActive(transactionsNav, false);
    }

}

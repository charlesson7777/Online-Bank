 /*
 * SidenavManager.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Specific sidenav for the manager
 */
package UI;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;

public class SidenavManager extends Sidenav {
    protected JButton homeNav;
    protected JButton clientsNav;
    protected JButton stocksNav;

    public SidenavManager(OnlineBankManager mainFrame) {
        super(mainFrame);
        homeNav = createNavElement("Home", false);
        clientsNav = createNavElement("Clients", false);
        stocksNav = createNavElement("Stocks", false);
        navigationPanel.add(homeNav);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(clientsNav);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(stocksNav);
    }

    public void initialize() {
        homeNav.addActionListener(e -> mainFrame.switchPanel("home")); 
        clientsNav.addActionListener(e -> mainFrame.switchPanel("clients")); 
        stocksNav.addActionListener(e -> mainFrame.switchPanel("stocks")); 
    }

    public JButton getHomeNav() {
        return homeNav;
    }

    public JButton getClientsNav() {
        return clientsNav;
    }

    public JButton getStocksNav() {
        return stocksNav;
    }


    // sets them all to false
    public void resetNavButtons() {
        setActive(homeNav, false);
        setActive(clientsNav, false);
        setActive(stocksNav, false);
    }
    
}

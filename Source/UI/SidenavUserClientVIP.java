 /*
 * SidenavUserClientVIP.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Specific sidenav for VIP clients
 */
package UI;
import java.awt.Dimension;

import javax.swing.JButton;

import javax.swing.Box;

public class SidenavUserClientVIP extends SidenavUserClient {
    protected JButton stocksNav;
    protected JButton stocksMarketNav;

    public SidenavUserClientVIP(OnlineBank mainFrame) {
        super(mainFrame);
        stocksNav = createNavElement("My Stocks", false);
        stocksMarketNav = createNavElement("Stock Market", false);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(stocksNav);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, navButtonMargins)));
        navigationPanel.add(stocksMarketNav);
    }

    public void initialize() {
        homeNav.addActionListener(e -> mainFrame.switchPanel("home")); 
        accountsNav.addActionListener(e -> mainFrame.switchPanel("accounts")); 
        transactionsNav.addActionListener(e -> mainFrame.switchPanel("transactions")); 
        stocksNav.addActionListener(e -> mainFrame.switchPanel("stocks")); 
        stocksMarketNav.addActionListener(e -> mainFrame.switchPanel("market")); 
    }

    public JButton getStocksNav() {
        return stocksNav;
    }


    public JButton getStocksMarketNav() {
        return stocksMarketNav;
    }

    public void resetNavButtons() {
        setActive(homeNav, false);
        setActive(accountsNav, false);
        setActive(transactionsNav, false);
        setActive(stocksNav, false);
        setActive(stocksMarketNav, false);
    }

}

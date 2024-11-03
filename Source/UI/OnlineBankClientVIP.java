 /*
 * OnlineBankVIP.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Hosts all UI elements for a VIP client's view of the online bank
 */
package UI;

import BankObjects.User;
import Communication.InstanceManager;

public class OnlineBankClientVIP extends OnlineBankClient {
    private SidenavUserClientVIP sidenav;

    public OnlineBankClientVIP(User user, InstanceManager commManager) {
        super(user, commManager);
    }

    public void initializeMainFrame() {
        super.initializeMainFrame();
        this.sidenav = new SidenavUserClientVIP(this);
        sidenav.setAccountName(firstname + " " + lastname);
    }

    protected void switchPanel(String panel) {
        mainFrame.getContentPane().removeAll();

        switch (panel.toUpperCase()) {
            case "HOME": 
                activePanel = new PanelClientHome(sidenav, this);
                ((PanelClientHome)activePanel).setName(firstname);
                ((PanelClientHome)activePanel).loadActivity();
                break;
            case "ACCOUNTS": 
                activePanel = new PanelClientAccounts(sidenav, this);
                break;
            case "TRANSACTIONS":
                activePanel = new PanelClientTransactions(sidenav, this);
                break;
            case "STOCKS":
                activePanel = new PanelClientStocks(sidenav, this);
                break;
            case "MARKET":
                activePanel = new PanelClientStockMarket(sidenav, this);
                break;
            default:
                activePanel = new PanelClientHome(sidenav, null);
                break;
        }

        mainFrame.add(activePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
}

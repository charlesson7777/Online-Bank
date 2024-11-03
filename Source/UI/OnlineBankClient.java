 /*
 * OnlineBankClient.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Hosts all the UI elements for a normal client's view of the online bank
 */
package UI;

import BankObjects.User;
import Communication.InstanceManager;

public class OnlineBankClient extends OnlineBank {
    private SidenavUserClient sidenav;
    protected PanelClient activePanel;
    protected String firstname; 
    protected String lastname;

    public OnlineBankClient(User user, InstanceManager commManager) {
        super(user, commManager);
        firstname = user.getFirstName();
        lastname = user.getLastName();
        activePanel = null;
    }

    public void initializeMainFrame() {
        super.initializeMainFrame();
        sidenav = new SidenavUserClient(this);
        sidenav.setAccountName(firstname + " " + lastname);
    }

    public void setName(String first, String last) {
        first = first.substring(0, 1).toUpperCase() + first.substring(1).toLowerCase();
        last = last.substring(0, 1).toUpperCase() + last.substring(1).toLowerCase();
        sidenav.setAccountName(first + " " + last);
        firstname = first;
        lastname = last;
    }


    // reuse sidenav because it doesn't contain a lot of information 
    // do not reuse panels because it contains a lot of information
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
            default:
                activePanel = new PanelClientHome(sidenav, null);
                break;
        }

        mainFrame.add(activePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void switchPanelWrapper(String panel) {
        executeInBackground(() -> switchPanel(panel));
    } 

    public PanelClient getActivePanel() {
        return activePanel;
    }
    
}

    

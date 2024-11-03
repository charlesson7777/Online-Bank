 /*
 * OnlineBankManager.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Hosts all UI elements for the manager's view of the online bank
 */
package UI;

import BankObjects.User;
import BankObjects.UserManager;
import Communication.InstanceManager;

public class OnlineBankManager extends OnlineBank {
    protected PanelManager activePanel;
    protected String firstname; 
    protected String lastname;
    protected SidenavManager sidenav;
    protected UserManager user;

    public OnlineBankManager(UserManager user, InstanceManager manager) {
        super((User)user, manager);
        this.user = user;
        firstname = user.getFirstName();
        lastname = user.getLastName();
        activePanel = null;
    }


    public void initializeMainFrame() {
        super.initializeMainFrame();
        sidenav = new SidenavManager(this);
        sidenav.setAccountName("Administrator");
    }

    public void setName(String first, String last) {
        first = first.substring(0, 1).toUpperCase() + first.substring(1).toLowerCase();
        last = last.substring(0, 1).toUpperCase() + last.substring(1).toLowerCase();
        sidenav.setAccountName("Administrator");
        firstname = first;
        lastname = last;
    }


    // reuse sidenav because it doesn't contain a lot of information 
    // do not reuse panels because it contains a lot of information
    protected void switchPanel(String panel) {
        mainFrame.getContentPane().removeAll();

        switch (panel.toUpperCase()) {
            case "HOME": 
                activePanel = new PanelManagerHome(sidenav, this);
                ((PanelManagerHome)activePanel).loadActivity();
                break;
            case "CLIENTS": 
                activePanel = new PanelManagerClients(sidenav, this);
                break;
            case "STOCKS":
                activePanel = new PanelManagerStocks(sidenav, this);
                break;
            default:
                activePanel = new PanelManagerHome(sidenav, null);
                break;
        }

        mainFrame.add(activePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void switchPanelWrapper(String panel) {
        executeInBackground(() -> switchPanel(panel));
    } 

    public PanelManager getActivePanel() {
        return activePanel;
    
    }
    
}

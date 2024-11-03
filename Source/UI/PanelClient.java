 /*
 * PanelClient.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * UI structure of a client main page
 */
package UI;
import javax.swing.*;

import Communication.FetcherClient;
import UI.Forms.Form;

import java.awt.*;


public abstract class PanelClient extends JPanel {    
    protected SidenavUserClient sidenav;
    protected PanelActivity activityPanel; 
    protected Dimension screenSize;
    protected OnlineBankClient mainFrame;
    protected final double SIDENAV_RATIO = 0.2;
    protected final double ACTIVITY_RATIO = 0.8;
    protected FetcherClient fetcher;

    public PanelClient(SidenavUserClient nav, PanelActivity activity, OnlineBankClient mainFrame) {
        this.mainFrame = mainFrame;
        sidenav = nav;
        this.activityPanel = activity;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public PanelClient(SidenavUserClient nav, OnlineBankClient mainFrame) {
        this(nav, null, mainFrame);
    }

    public PanelClient(OnlineBankClient mainFrame) {
        this(null, null, mainFrame);
    }

    protected void initSidenav(SidenavUserClient nav, OnlineBank mainFrame) {
        nav.initialize();
        nav.resetNavButtons();
        setSidenav(nav);
    }

    public abstract void loadActivity();
    protected abstract void setActivityPanel();

    protected void initialize() {
        setLayout(new BorderLayout());

        int sidenavWidth = (int) (screenSize.width * SIDENAV_RATIO);
        JPanel sidenavWrapper = new JPanel(new BorderLayout());
        sidenavWrapper.setPreferredSize(new Dimension(sidenavWidth, screenSize.height));
        sidenavWrapper.add(sidenav, BorderLayout.CENTER);
        add(sidenavWrapper, BorderLayout.WEST);

        int activityWidth = (int) (screenSize.width * ACTIVITY_RATIO);
        JPanel activityWrapper = new JPanel(new BorderLayout());
        activityWrapper.setPreferredSize(new Dimension(activityWidth, screenSize.height));
        activityWrapper.add(activityPanel, BorderLayout.CENTER);
        add(activityWrapper, BorderLayout.CENTER);
    }

    public Sidenav getSidenav() {
        return sidenav;
    }

    public void setSidenav(SidenavUserClient nav) {
        sidenav = nav;
    }

    protected void setActivityPanel(PanelActivity activity) {
        activityPanel = activity;
    }

    protected JPanel getActivityPanel() {
        return activityPanel;
    }

    protected void initDefaultActivityPanel(String activityName) {
        activityPanel = new PanelActivity(activityName);
    }

    protected void openForm(Form formPanel) {
        formPanel.setLocationRelativeTo(null); 
        formPanel.setVisible(true);
    }


}

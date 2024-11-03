 /*
 * PanelManager.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Generic panel for a manager view
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

import Communication.FetcherManager;
import UI.Forms.Form;

public abstract class PanelManager extends JPanel {
    protected SidenavManager sidenav;
    protected PanelActivity activityPanel; 
    protected Dimension screenSize;
    protected OnlineBankManager mainFrame;
    protected final double SIDENAV_RATIO = 0.2;
    protected final double ACTIVITY_RATIO = 0.8;
    protected FetcherManager fetcher;

    public PanelManager(SidenavManager nav, PanelActivity activity, OnlineBankManager mainFrame) {
        this.mainFrame = mainFrame;
        sidenav = nav;
        this.activityPanel = activity;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public PanelManager(SidenavManager nav, OnlineBankManager mainFrame) {
        this(nav, null, mainFrame);
    }

    public PanelManager(OnlineBankManager mainFrame) {
        this(null, null, mainFrame);
    }

    protected void initSidenav(SidenavManager nav, OnlineBankManager mainFrame) {
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

    public void setSidenav(SidenavManager nav) {
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


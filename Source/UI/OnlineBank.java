 /*
 * OnlineBank.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Generic class to represent the online bank
 */
package UI;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import BankObjects.User;
import Communication.InstanceManager;
import UI.Theme.TextStyle;

public abstract class OnlineBank {
    protected Dimension screenSize;
    protected JFrame mainFrame;
    protected User user;
    protected InstanceManager commManager;

    public OnlineBank(User user, InstanceManager manager) {
        this.user = user;
        commManager = manager;
        initializeMainFrame();
    }

    public void setCommManager(InstanceManager manager) {
        commManager = manager;
    }

    public InstanceManager getCommManager() {
        return commManager;
    }

    public void initializeMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Online Banking");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(screenSize); 
        mainFrame.setLocationRelativeTo(null);
        TextStyle.setDefaultFont();
    }

    
    public void loadInitialPanel() {
        switchPanel("home");
        SwingUtilities.invokeLater(() -> {
            mainFrame.setVisible(true);
        });
    }
    
    // reuse sidenav because it doesn't contain a lot of information 
    // do not reuse panels because it contains a lot of information
    protected abstract void switchPanel(String panel);
    protected abstract void switchPanelWrapper(String panel);
    public abstract void setName(String first, String last);

    protected void executeInBackground(Runnable task) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                task.run();
                return null;
            }
        };
        worker.execute();
    }

    public void close() {
        mainFrame.dispose();
    }

    public User getUser() {
        return user;
    }

    
}

    

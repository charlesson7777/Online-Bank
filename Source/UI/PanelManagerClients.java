 /*
 * PanelManagerClients.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Clients page that lists all the clients of the bank
 */
package UI;

import java.awt.Color;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BankObjects.UserClient;
import BankObjects.UserClientVIP;
import Communication.Adapter;
import Communication.FetcherManagerClient;
import UI.Forms.FormViewClient;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.TextStyle;

public class PanelManagerClients extends PanelManager {
    List<PanelUserHolder> panelsList;
    JPanel accountsPanel;

    public PanelManagerClients(SidenavManager nav, OnlineBankManager mainFrame) {
        super(nav, mainFrame);
        panelsList = new ArrayList<>();
        accountsPanel = new JPanel();
        fetcher = new FetcherManagerClient();
        initSidenav(nav, mainFrame);
        loadActivity();
    }

    public PanelManagerClients(OnlineBankManager mainFrame) {
        this(new SidenavManager(mainFrame), mainFrame);
    }

    public void loadActivity() {
        super.initDefaultActivityPanel(MyStrings.ACCOUNTS_TITLE);
        setActivityPanel();
        super.initialize();
    }

    protected void initSidenav(SidenavManager nav, OnlineBankManager mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getClientsNav(), true);
    }

    protected void setActivityPanel() {
        // title
        JPanel titleContainer = activityPanel.getTitleContainer();
        int padding = screenSize.width / ((10 * 8) / 1);
        activityPanel.getTitle().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, padding));
        
        // add account button
        RoundedButton viewClientButton = new RoundedButton("View Client");
        viewClientButton.setForeground(Pallete.MEDIUM_GREY);
        viewClientButton.setFont(TextStyle.BODY_FONT);
        viewClientButton.setColor(Pallete.PALE_BLUE);
        viewClientButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        viewClientButton.setBorderPainted(false);
        viewClientButton.setBorder(BorderFactory.createEmptyBorder(padding/2, padding, padding/2, padding));
        titleContainer.add(viewClientButton);
        
        // scrollable screen
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));
        JPanel content = activityPanel.getActivity();
        content.setLayout(new BorderLayout());
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);
        accountsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, marginY));
        content.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, 0));
        
        // accounts
        accountsPanel.setOpaque(true);
        accountsPanel.setBackground(Color.WHITE);

        updateClients();
        updateScroll();        
        
        viewClientButton.addActionListener(e -> openForm(
            new FormViewClient(mainFrame.getCommManager(), (FetcherManagerClient)fetcher)
        ));
    }

    public void updateClients() {
       fetcher.fetch(null);
       List<UserClient> normalClients = ((FetcherManagerClient)fetcher).getNormalClients();
       List<UserClientVIP> VIPClients = ((FetcherManagerClient)fetcher).getVIPClients();
       String[][] toAdd; 
       Adapter adapter = new Adapter();
       if (!VIPClients.isEmpty()) {
            addAccountsPanel("VIP Clients");
            toAdd = adapter.parseClientInfo(VIPClients);
            panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
            panelsList.get(panelsList.size() - 1).updateSize();
        }
            
        if (!normalClients.isEmpty()) {
            addAccountsPanel("Non-VIP Clients");
            toAdd = adapter.parseClientInfo(normalClients);
            panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
            panelsList.get(panelsList.size() - 1).updateSize();
        }
    }
    

    // adds a scroll if necessary
    public void updateScroll() {
        JPanel content = activityPanel.getActivity();
        int marginsY = screenSize.height  / (int) ((5.5 * 8) / 1);
        int activitySize = (int)(screenSize.height - (marginsY * 2 + marginsY * 0.5));
        int accountsPanelHeight = countAcccountsPanelHeight();
    
        if (accountsPanelHeight > activitySize) {
            MinimalScrollPane scroll = new MinimalScrollPane(accountsPanel, Color.WHITE, Pallete.PALE_GREY);
            content.removeAll(); 
            content.setLayout(new BorderLayout());
            content.add(scroll, BorderLayout.CENTER);

        } else {
            content.removeAll(); 
            content.setLayout(new BorderLayout());
            content.add(accountsPanel, BorderLayout.NORTH);
        }
        revalidate(); 
        repaint(); 
    }

    private int countAcccountsPanelHeight() {
        int padding = (int)(screenSize.width / ((10 * 8) / 1));
        int height = 0;
        for (int i = 0; i < panelsList.size(); i++) {
            height += panelsList.get(i).getHeight();
            // padding from the title
            height += padding * 3;
            if (i > 0) {
                height += padding;
            }
        }

        return height;
    }    

    

    public void addAccountsPanel(String name) {
        int padding = (int)(screenSize.width / ((10 * 8) / 1));

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        JLabel title = new JLabel(name);
        title.setForeground(Pallete.DARK_GREY);
        title.setFont(TextStyle.SUB_HEADING_2_FONT);
        if (!panelsList.isEmpty()) {
            title.setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));
        } else {
            title.setBorder(BorderFactory.createEmptyBorder(0, 0, padding, 0));
        }

        PanelUserHolder newPanel = new PanelUserHolder(screenSize, name);
        
        panelsList.add(newPanel);
    
        container.add(title, BorderLayout.NORTH);
        container.add(newPanel, BorderLayout.CENTER);
        
        accountsPanel.add(container);
    }
    
}

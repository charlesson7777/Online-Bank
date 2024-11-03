 /*
 * PanelClientAccounts.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Accounts page of a client page
 */
package UI;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import BankObjects.TypeAccount;
import BankObjects.User;
import BankObjects.UserPrivilege;
import Communication.Adapter;
import Communication.FetcherAccounts;
import UI.Forms.FormCloseAccount;
import UI.Forms.FormNewAccount;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.TextStyle;

public class PanelClientAccounts extends PanelClient {
    List<PanelAccountsHolder> panelsList;
    JPanel accountsPanel;

    public PanelClientAccounts(SidenavUserClient nav, OnlineBankClient mainFrame) {
        super(nav, mainFrame);
        panelsList = new ArrayList<>();
        accountsPanel = new JPanel();
        fetcher = new FetcherAccounts();
        initSidenav(nav, mainFrame);
        loadActivity();
    }

    // default is non-vip client
    public PanelClientAccounts(OnlineBankClient mainFrame) {
        this(new SidenavUserClient(mainFrame), mainFrame);
    }

    public void loadActivity() {
        super.initDefaultActivityPanel(MyStrings.ACCOUNTS_TITLE);
        setActivityPanel();
        super.initialize();
    }

    protected void initSidenav(SidenavUserClient nav, OnlineBank mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getAccountsNav(), true);
    }

    protected void setActivityPanel() {
        // title
        JPanel titleContainer = activityPanel.getTitleContainer();
        int padding = screenSize.width / ((10 * 8) / 1);
        activityPanel.getTitle().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, padding));
        
        // add account button
        RoundedButton newAccountButton = new RoundedButton("+");
        newAccountButton.setForeground(Pallete.MEDIUM_GREY);
        newAccountButton.setFont(TextStyle.SUB_HEADING_2_FONT);
        newAccountButton.setColor(Pallete.PALE_BLUE);
        newAccountButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        newAccountButton.setBorderPainted(false);
        newAccountButton.addActionListener(e -> processNewAccount());
        titleContainer.add(newAccountButton);

        // close account button
        RoundedButton closeAccountButton = new RoundedButton("-");
        closeAccountButton.setForeground(Pallete.MEDIUM_GREY);
        closeAccountButton.setFont(TextStyle.SUB_HEADING_2_FONT);
        closeAccountButton.setColor(Pallete.PALE_BLUE);
        closeAccountButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        closeAccountButton.setBorderPainted(false);
        closeAccountButton.addActionListener(e -> deleteAccount());
        titleContainer.add(closeAccountButton);

        
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
        updateAccounts();

        updateScroll();        
        
    }

    private void processNewAccount() {
        openForm(new FormNewAccount(mainFrame.getCommManager(), mainFrame.getUser()));
        updateAccounts();
    }

    private void deleteAccount() {
        openForm(new FormCloseAccount(mainFrame.getCommManager(), mainFrame.getUser(), (FetcherAccounts)fetcher));
        updateAccounts();
    }

    private void checkUser() {
        UserPrivilege currPrivilege = mainFrame.getUser().getPrivilege();
        User updatedUser = mainFrame.getCommManager().checkUserPrivilege(mainFrame.getUser());
        if (updatedUser.getPrivilege() != currPrivilege) {
            String message = "";
            if (updatedUser.getPrivilege() == UserPrivilege.VIP) {
                message = "You have been promoted to VIP. Please logout and login again.";
            } else if (updatedUser.getPrivilege() == UserPrivilege.NORMAL) {
                message = "You have lost VIP benefits. Please logout and login again.";
            }

            Object[] options = {"I understand."};
            JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
            JDialog dialog = optionPane.createDialog(null, "User Privilege Update");
            
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    forceClose(dialog);
                }
            });

            dialog.setVisible(true);
        }
    }

    private void forceClose(JDialog dialog) {
        dialog.dispose(); 
        mainFrame.getCommManager().logout();
    }

    public void updateAccounts() {
        checkUser();
        fetcher.fetch(mainFrame.getUser().getID());
        FetcherAccounts accFetcher = ((FetcherAccounts) fetcher);
        List<TypeAccount> accountTypes = accFetcher.getAccountTypes();
    
        for (TypeAccount type : accountTypes) {
            boolean panelExists = false;
            String[][] toAdd;
            for (PanelAccountsHolder panel : panelsList) {
                if (type == TypeAccount.LOAN && panel.getAccountType().equals(type)) {
                    panelExists = true;
                    toAdd = Adapter.parseBankAccountList(accFetcher.getLoanAccounts());
                    panel.getTable().setData(toAdd);
                    panel.updateSize();
                    break;
                    
                } else if (panel.getAccountType().equals(type)) {
                    panelExists = true;
                    toAdd = Adapter.parseBankAccountList(accFetcher.getAccountsOfType(type));
                    panel.getTable().setData(toAdd);
                    panel.updateSize();
                    break;
                }
                
            }

            if (!panelExists) {
                addAccountsPanel(type.toString());
                if (type == TypeAccount.LOAN) {
                    toAdd = Adapter.parseBankAccountList(accFetcher.getLoanAccounts());
                    panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
                    panelsList.get(panelsList.size() - 1).updateSize();

                } else {
                    toAdd = Adapter.parseBankAccountList(accFetcher.getAccountsOfType(type));
                    panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
                    panelsList.get(panelsList.size() - 1).updateSize();
                }
                
            }
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
        
        String formattedTitle = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        JLabel title = new JLabel(formattedTitle);
        title.setForeground(Pallete.DARK_GREY);
        title.setFont(TextStyle.SUB_HEADING_2_FONT);
        if (!panelsList.isEmpty()) {
            title.setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));
        } else {
            title.setBorder(BorderFactory.createEmptyBorder(0, 0, padding, 0));
        }
        
        PanelAccountsHolder newPanel;
        if (name.equals(TypeAccount.LOAN.toString())) {
            newPanel = new PanelAccountsHolder(screenSize, name);
        } else {
            newPanel = new PanelAccountsHolder(screenSize, name);
        }
        
        panelsList.add(newPanel);
    
        container.add(title, BorderLayout.NORTH);
        container.add(newPanel, BorderLayout.CENTER);
        
        accountsPanel.add(container);
    }



}


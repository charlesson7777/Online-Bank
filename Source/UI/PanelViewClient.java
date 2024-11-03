package UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BankObjects.TypeAccount;
import Communication.Adapter;
import Communication.FetcherAccounts;
import Communication.FetcherTransactions;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.TextStyle;

public class PanelViewClient extends JFrame {
    protected PanelActivity activityPanel;
    protected List<PanelAccountsHolder> panelsList;
    protected Dimension screenSize;
    protected JPanel accountsPanel;
    protected UUID userID;
    private MinimalTable transactionsTable;

    public PanelViewClient(String type, String clientName, UUID uid) {
        String panelName;
        type = type.toUpperCase();
        if (type.equals("ACCOUNTS")) {
            panelName = "Client Accounts";
        } else {
            panelName = "Client Transactions";
        }
    
        setTitle(panelName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userID = uid;
    
        String activityName;
        if (type.equals("ACCOUNTS")) {
            activityName = clientName + "'s Accounts";
        } else {
            activityName = clientName + "'s Transactions";
        }
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8);
        int height = (int) (screenSize.getHeight() * 0.8);
        screenSize = new Dimension(width, height);
        activityPanel = new PanelActivity(activityName);
        accountsPanel = new JPanel();
        panelsList = new ArrayList<>();
        getContentPane().add(activityPanel);
        
        setSize(width, height);
        setLocationRelativeTo(null); 
    
        setActivity(type);
    }    

    public void setActivity(String viewType) {
        switch(viewType.toUpperCase()) {
            case "ACCOUNTS":
                setAccountsActivity();
                break;
            case "TRANSACTIONS":
                setTransactionsActivity();
                break;
            default:
                break;
        }
    }

    private void setTransactionsActivity() {
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);

        transactionsTable = new MinimalTable(screenSize, MyStrings.TRANSACTIONS_HEADERS.length, Color.WHITE);
        transactionsTable.setHeaders(MyStrings.TRANSACTIONS_HEADERS, Color.WHITE, Pallete.DARK_GREY, true);
        transactionsTable.setRowColors(Color.WHITE, Color.WHITE, Pallete.DARK_GREY);
        transactionsTable.setBorderStyle(false);

        MinimalScrollPane scrollPane = new MinimalScrollPane(transactionsTable, Color.WHITE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, marginX));
        activityPanel.add(scrollPane, BorderLayout.CENTER);

        loadTransactions();

    }

    private void loadTransactions() {
        FetcherTransactions fetcherTransactions = new FetcherTransactions();
        fetcherTransactions.fetch(userID);
        String[][] data = Adapter.parseDailyTxn(fetcherTransactions.getAllTxn());
        updateTransactionsTable(data);
    }

    private void updateTransactionsTable(String[][] transactions) {
        if (transactions == null) {
            JLabel noTransactions = new JLabel("No transactions made.");
            noTransactions.setFont(TextStyle.BODY_FONT);
            noTransactions.setForeground(Pallete.LIGHT_GREY);

            int marginX = (int) (screenSize.width / ((10.0 * 8) / 4));
            JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            container.setOpaque(false);
            container.setBorder(BorderFactory.createEmptyBorder(0, marginX, 0, 0));
            container.add(noTransactions);
            
            activityPanel.add(container, BorderLayout.CENTER);
        } else {
            transactionsTable.setData(transactions);
        }

    }

    private void setAccountsActivity() {
        
        // scrollable screen
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));
        JPanel content = activityPanel.getActivity();
        content.setLayout(new BorderLayout());
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);
        accountsPanel.setBorder(BorderFactory.createEmptyBorder(0, marginX, 0, marginY));
        content.setBorder(BorderFactory.createEmptyBorder(0, 0, marginY, 0));
        
        // accounts
        accountsPanel.setOpaque(true);
        accountsPanel.setBackground(Color.WHITE);
        updateAccounts();

        updateScroll();        
    }

    public void updateAccounts() {
        FetcherAccounts fetcher = new FetcherAccounts();
        fetcher.fetch(userID);
        List<TypeAccount> accountTypes = fetcher.getAccountTypes();
    
        for (TypeAccount type : accountTypes) {
            boolean panelExists = false;
            String[][] toAdd;
            for (PanelAccountsHolder panel : panelsList) {
                if (type == TypeAccount.LOAN && panel.getAccountType().equals(type)) {
                    panelExists = true;
                    toAdd = Adapter.parseBankAccountList(fetcher.getLoanAccounts());
                    panel.getTable().setData(toAdd);
                    panel.updateSize();
                    break;
                    
                } else if (panel.getAccountType().equals(type)) {
                    panelExists = true;
                    toAdd = Adapter.parseBankAccountList(fetcher.getAccountsOfType(type));
                    panel.getTable().setData(toAdd);
                    panel.updateSize();
                    break;
                }
                
            }

            if (!panelExists) {
                addAccountsPanel(type.toString());
                if (type == TypeAccount.LOAN) {
                    toAdd = Adapter.parseBankAccountList(fetcher.getLoanAccounts());
                    panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
                    panelsList.get(panelsList.size() - 1).updateSize();

                } else {
                    toAdd = Adapter.parseBankAccountList(fetcher.getAccountsOfType(type));
                    panelsList.get(panelsList.size() - 1).getTable().setData(toAdd);
                    panelsList.get(panelsList.size() - 1).updateSize();
                }
                
            }
        }

        if (accountTypes.isEmpty()) {
            JLabel noAccounts = new JLabel("This client has no accounts.");
            noAccounts.setForeground(Pallete.LIGHT_GREY);

            JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            int marginsY = (int)(screenSize.width / (10.0 * 8) * 1);
            container.setBorder(BorderFactory.createEmptyBorder(marginsY, 0, 0, 0));
            container.setOpaque(false);
            container.add(noAccounts);
            accountsPanel.add(container);
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

    public void loadActivity() {
        setVisible(true);
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

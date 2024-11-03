 /*
 * PanelClientHome.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Home page for a client
 */
package UI;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.awt.event.*;

import BankObjects.BankAccount;
import BankObjects.User;
import BankObjects.UserPrivilege;
import Communication.Adapter;
import Communication.FetcherAccounts;
import Communication.FetcherTransactions;
import Communication.InstanceManager;
import UI.Forms.FormDeposit;
import UI.Forms.FormLoans;
import UI.Forms.FormNewAccount;
import UI.Forms.FormNewAccountVIP;
import UI.Forms.FormTransfer;
import UI.Forms.FormWithdraw;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.RoundedPanel;
import UI.Theme.TextStyle;

public class PanelClientHome extends PanelClient {
    private String firstname;
    
    // action buttons
    private RoundedButton transferButton;
    private RoundedButton depositButton;
    private RoundedButton withdrawButton;
    private RoundedButton loansButton;
    private RoundedButton[] mainActionButtons;
    
    // recent transactions
    private MinimalTable transactionsTable;
    private JPanel tableWrapper;
    private FetcherTransactions fetcherTransactions;
    private JPanel noTransactionsContainer;

    // add accounts 
    private RoundedButton addAccountsButton;
    private JPanel accountsPanel;
    private int numAccountsShown;
    private JPanel accountsList[];

    private JPanel rightPanel;
    private JPanel leftPanel;

    private final double LEFT_PANE_RATIO = 4.0 / 6.0;
    private final int MAX_ACCOUNT_PANES = 3;


    public PanelClientHome(SidenavUserClient nav, OnlineBankClient mainFrame) {
        super(nav, mainFrame);
        numAccountsShown = 0;
        accountsList = new JPanel[MAX_ACCOUNT_PANES];
        firstname = "Talia";
        initSidenav(nav, mainFrame);
    }

    // default is non-vip
    public PanelClientHome(OnlineBankClient mainFrame) {
        this(new SidenavUserClient(mainFrame), mainFrame);
    }

    public void setName(String name) {
        firstname = name;
    }

    protected void initSidenav(SidenavUserClient nav, OnlineBankClient mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getHomeNav(), true);
    }

    public void loadActivity() {
        String title = MyStrings.HOME_TITLE + ", " + firstname + "!";
        super.initDefaultActivityPanel(title);
        setActivityPanel();

        loadAccounts();
        resetAccountsPanel();
        loadTransactions();
        super.initialize();
    }

    private void loadTransactions() {
        fetcherTransactions = new FetcherTransactions();
        fetcherTransactions.fetch(mainFrame.getUser().getID());
        String[][] data = Adapter.parseDailyTxn(fetcherTransactions.getDailyTxn());
        updateTransactionsTable(data);
    }

    public void loadAccounts() {
        numAccountsShown = 0;
        accountsList = new JPanel[MAX_ACCOUNT_PANES];
        fetcher = new FetcherAccounts();
        fetcher.fetch(mainFrame.getUser().getID());

        List<BankAccount> topAccounts = ((FetcherAccounts)fetcher).getTopAccounts();

        for (BankAccount account : topAccounts) {
            String type = account.getAccountType() + " - " + 
                String.valueOf(account.getID()).substring(12);
            String balance = String.format("%.2f", account.getBalance());
            String currency = account.getCurrency().getName();

            addNewAccountPanel(type, balance, currency);
        }
    }

    protected void setActivityPanel() {
        initButtons();
    
        JPanel content = activityPanel.getActivity();
        content.setLayout(new BorderLayout());
    
        int marginsY = (int)(screenSize.width / (10.0 * 8) * 4);
        int contentWidth = activityPanel.getActivityWidth();
        int lWidth = (int)(contentWidth * LEFT_PANE_RATIO) - marginsY;
        int rWidth = contentWidth - lWidth;
    
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(lWidth, content.getHeight()));
        rightPanel.setPreferredSize(new Dimension(rWidth, content.getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, marginsY));
    
        leftPanel.setBackground(Color.RED);
        rightPanel.setBackground(Color.BLUE);

        addActionButtons(leftPanel);
        addTransactionsTable(leftPanel);
        addAccountsPanel(rightPanel);
    
        content.add(leftPanel, BorderLayout.WEST);
        content.add(rightPanel, BorderLayout.CENTER);
    
    } 

    public void updateAccounts() {
        checkUser();
        loadAccounts();
        resetAccountsPanel();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void updateTransactions() {
        loadTransactions();
        leftPanel.revalidate();
        leftPanel.repaint();
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

        
    
    private void resetAccountsPanel() {
        accountsPanel.removeAll();

        for (int i = 0; i < numAccountsShown; i++) {
            accountsPanel.add(accountsList[i]);
        }

        if (numAccountsShown == 0) {
            JLabel noAccounts = new JLabel("No accounts listed. Create a new account.");
            noAccounts.setForeground(Pallete.LIGHT_GREY);

            JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            int marginsY = (int)(screenSize.width / (10.0 * 8) * 1);
            container.setBorder(BorderFactory.createEmptyBorder(marginsY, 0, 0, 0));
            container.setOpaque(false);
            container.add(noAccounts);

            rightPanel.add(container, BorderLayout.CENTER);
        }
    }   

    public void addAccountsPanel(JPanel panel) {
        int margin = (int)(screenSize.width / (10.0 * 8) * 1.75);

        JPanel titleHolder = new JPanel();
        JLabel title = new JLabel("Your Accounts");
        title.setFont(TextStyle.SUB_HEADING_2_FONT);
        title.setForeground(Pallete.DARK_GREY);
        addAccountsButton = new RoundedButton("+");
        addAccountsButton.setFont(TextStyle.SUB_HEADING_2_FONT);
        addAccountsButton.setColor(Pallete.PALE_BLUE);
        addAccountsButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        addAccountsButton.setForeground(Pallete.MEDIUM_GREY);
        addAccountsButton.addActionListener(e -> 
            openNewAccount()
        );
            
        
        titleHolder.setOpaque(false);
        titleHolder.setLayout(new BorderLayout());
        titleHolder.add(title, BorderLayout.WEST);
        titleHolder.add(addAccountsButton, BorderLayout.EAST);

        accountsPanel = new JPanel();
        accountsPanel.setLayout(new GridLayout(MAX_ACCOUNT_PANES, 1, 0, margin));
        accountsPanel.setBackground(Color.BLUE);
        accountsPanel.setBorder(BorderFactory.createEmptyBorder(margin, 0, 0, 0));
        accountsPanel.setOpaque(false);

        panel.add(titleHolder, BorderLayout.NORTH);
        panel.add(accountsPanel, BorderLayout.CENTER);
        
    }

    private void openNewAccount() {
        if (mainFrame.getUser().getPrivilege() == UserPrivilege.NORMAL) {
            openForm(
                new FormNewAccount(mainFrame.getCommManager(), mainFrame.getUser()));
        } else {
            openForm(new FormNewAccountVIP(
                mainFrame.getCommManager(),
                mainFrame.getUser(), 
                (FetcherAccounts)fetcher
                ));
        }

    }

    public void setNewAccountPanel(int rowNum, String name, String amount, String currency) {
        if (rowNum < numAccountsShown) {
            JPanel newPanel = createNewAccountPanel(name, amount, currency);
            accountsList[rowNum] = newPanel;
        }
    }

    public void addNewAccountPanel(String name, String amount, String currency) {
        if (numAccountsShown < MAX_ACCOUNT_PANES) {
            JPanel newPanel = createNewAccountPanel(name, amount, currency);
            accountsList[numAccountsShown++] = newPanel;
        }
    }

    private JPanel createNewAccountPanel(String name, String amount, String currency) {
        int margin = (int)(screenSize.width / (10.0 * 8) * 1.5);
        int balanceMargin = (int)(screenSize.width / (10.0 * 8) * 0.25);
        int topMargin = (int)(screenSize.width / (10.0 * 8) * 4);

        RoundedPanel newPanel = new RoundedPanel();
        newPanel.setLayout(new BorderLayout());
        newPanel.setBorder(BorderFactory.createEmptyBorder(topMargin, margin, margin, margin));
        
        JLabel balanceMoney = new JLabel(amount);
        balanceMoney.setFont(TextStyle.SUB_HEADING_1_FONT);
        balanceMoney.setForeground(Pallete.DARK_GREY);
        JLabel currencyLabel = new JLabel(currency);
        currencyLabel.setFont(TextStyle.SUB_HEADING_1_BOLD);
        currencyLabel.setBorder(BorderFactory.createEmptyBorder(0, balanceMargin, 0, 0));
        JPanel balance = new JPanel();
        balance.setLayout(new BorderLayout());
        balance.setBorder(BorderFactory.createEmptyBorder(margin, 0, 0, 0));
        balance.add(balanceMoney, BorderLayout.WEST);
        balance.add(currencyLabel, BorderLayout.CENTER);
        balance.setOpaque(false);

        JLabel accountType = new JLabel(name);
        Font customFont = new Font(TextStyle.BODY_FONT.getFontName(), TextStyle.BODY_FONT.getStyle(), 14);
        accountType.setFont(customFont);
        accountType.setForeground(Pallete.DARK_GREY);
        accountType.setBackground(Color.BLUE);

        newPanel.add(balance, BorderLayout.CENTER);
        newPanel.add(accountType, BorderLayout.SOUTH);

        return newPanel;
    }
    

    public MinimalTable getTable() {
        return transactionsTable;
    }

    private void addTransactionsTable(JPanel panel) {
        int marginsY = (int)(screenSize.width / (10.0 * 8) * 4);
        int margins = (int)(screenSize.width / (10.0 * 8) * 1);
        tableWrapper = new JPanel();
        tableWrapper.setOpaque(false);
        tableWrapper.setLayout(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(marginsY, 0, 0, 0));

        JPanel titleBar = new JPanel();
        titleBar.setOpaque(false);
        JLabel title = new JLabel("Recent Transactions (Last 24 Hours)");
        title.setFont(TextStyle.SUB_HEADING_2_BOLD);
        titleBar.setLayout(new BorderLayout());
        titleBar.add(title, BorderLayout.WEST);
        tableWrapper.add(titleBar, BorderLayout.NORTH);


        int[] alignment = new int[] {JLabel.LEFT, JLabel.LEFT, JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT};
        int tableWidth = panel.getPreferredSize().width;
        int[] widths = new int[] {
            (int)(tableWidth * 1.25/5), 
            (int)(tableWidth * 0.5/5), 
            (int)(tableWidth * 1.0/5), 
            (int)(tableWidth * 1.0/5), 
            (int)(tableWidth * 0.25/5)
        };
        String[] headers = new String[] {"Title", "Date", "Price", "Account", "Currency"};

        transactionsTable = new MinimalTable(screenSize, 5, Color.WHITE);
        transactionsTable.setHeaders(headers, Color.WHITE, Pallete.DARK_GREY, false);
        transactionsTable.setHeaderAlignment(alignment);
        transactionsTable.setRowAlignment(alignment);
        transactionsTable.setDataColumnBold(4, true);
        transactionsTable.setColumnWidths(widths);
        transactionsTable.setTableHeader(null);
        transactionsTable.setLayout(new BorderLayout());

        MinimalScrollPane scrollPane = new MinimalScrollPane(transactionsTable, Color.WHITE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(margins, 0, 0, 0));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        panel.add(tableWrapper, BorderLayout.CENTER);
    }

    public void updateTransactionsTable(String[][] transactions) {
        if (transactions == null) {
            JLabel noTransactions = new JLabel("No transactions in the last 24 hours.");
            noTransactions.setFont(TextStyle.BODY_FONT);
            noTransactions.setForeground(Pallete.LIGHT_GREY);

            int marginsY = (int)(screenSize.width / (10.0 * 8) * 1);
            noTransactionsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            noTransactionsContainer.setOpaque(false);
            noTransactionsContainer.setBorder(BorderFactory.createEmptyBorder(marginsY, 0, 0, 0));
            noTransactionsContainer.add(noTransactions);
            
            tableWrapper.add(noTransactionsContainer, BorderLayout.CENTER);
        } else {
            if (noTransactionsContainer != null) {
                tableWrapper.remove(noTransactionsContainer);
            }
            tableWrapper.add(transactionsTable, BorderLayout.CENTER);
            transactionsTable.setData(transactions);
        }

    }

    private void addActionButtons(JPanel panel) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        int[] gridXLocs = new int[] {0, 1, 0, 1};
        int[] gridYLocs = new int[] {0, 0, 1, 1};
        int padding = (int)(screenSize.width / (10.0 * 8) * 1);
        Insets[] insets = new Insets[] {
            new Insets(0, 0, 0, 0), 
            new Insets(0, padding, 0, 0), 
            new Insets(padding, 0, 0, 0), 
            new Insets(padding, padding, 0, 0)
        };

        for (int i = 0; i < mainActionButtons.length; i++) {
            gbc.gridx = gridXLocs[i];
            gbc.gridy = gridYLocs[i];
            gbc.insets = insets[i];
            buttonsPanel.add(mainActionButtons[i], gbc);
        }

        panel.add(buttonsPanel, BorderLayout.NORTH);
    }

    private void initButtons() {
        transferButton = new RoundedButton("Transfer");
        depositButton = new RoundedButton("Deposit");
        withdrawButton = new RoundedButton("Withdraw");
        loansButton = new RoundedButton("Loans");
        mainActionButtons = new RoundedButton[] {transferButton, depositButton, withdrawButton, loansButton};

        int buttonWidth = (int) (1.5 * 8);
        buttonWidth = (int)(screenSize.width / (10 * 8) * buttonWidth);
        int buttonHeight = (int)(screenSize.width / (10 * 8) * 5);

        for (RoundedButton button : mainActionButtons) {
            button.setColor(Pallete.PALE_ORANGE);
            button.setHoverColor(Pallete.PALE_ORANGE_HOVER);
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        }

        InstanceManager commManager = mainFrame.getCommManager();
        transferButton.addActionListener(e -> openForm(
            new FormTransfer(commManager, mainFrame.getUser(), (FetcherAccounts)fetcher)));
        depositButton.addActionListener(e -> openForm(
            new FormDeposit(commManager, mainFrame.getUser(), (FetcherAccounts)fetcher)));
        withdrawButton.addActionListener(e -> openForm(
            new FormWithdraw(commManager, mainFrame.getUser(), (FetcherAccounts)fetcher)));
        loansButton.addActionListener(e -> openForm(
            new FormLoans(commManager, mainFrame.getUser(), (FetcherAccounts)fetcher)));
    }
    

    

    
}

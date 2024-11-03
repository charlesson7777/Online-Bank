 /*
 * PanelManagerHome.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Home page of a manager view
 */
package UI;

import java.awt.*;
import javax.swing.*;
import Communication.Adapter;
import Communication.FetcherManagerAccounts;
import Communication.FetcherManagerTxn;
import Communication.InstanceManager;
import UI.Forms.FormModStock;
import UI.Forms.FormNewStock;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.RoundedPanel;
import UI.Theme.TextStyle;

public class PanelManagerHome extends PanelManager {

    // action buttons
    private RoundedButton addStocksButton;
    private RoundedButton editStocksButton;
    //private RoundedButton removeStocksButton;
    //private RoundedButton viewLoansButton;
    private RoundedButton[] mainActionButtons;
    
    // recent transactions
    private MinimalTable transactionsTable;
    private JPanel tableWrapper;
    private JPanel noTxnContainer;
    private FetcherManagerTxn fetcherTransactions;

    // add accounts 
    private JPanel accountsPanel;
    private int numAccountsShown;
    private JPanel accountsList[];

    private JPanel rightPanel;
    private JPanel leftPanel;

    private final double LEFT_PANE_RATIO = 4.0 / 6.0;
    private final int MAX_ACCOUNT_PANES = 3;


    public PanelManagerHome(SidenavManager nav, OnlineBankManager mainFrame) {
        super(nav, mainFrame);
        numAccountsShown = 0;
        accountsList = new JPanel[MAX_ACCOUNT_PANES];
        initSidenav(nav, mainFrame);
    }

    // default is non-vip
    public PanelManagerHome(OnlineBankManager mainFrame) {
        this(new SidenavManager(mainFrame), mainFrame);
    }

    protected void initSidenav(SidenavManager nav, OnlineBankManager mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getHomeNav(), true);
    }

    public void loadActivity() {
        String title = "Welcome Back!";
        super.initDefaultActivityPanel(title);
        setActivityPanel();

        loadAccounts();
        loadTransactions();
        resetAccountsPanel();
        super.initialize();
    }

    public void loadAccounts() {
        numAccountsShown = 0;
        accountsList = new JPanel[MAX_ACCOUNT_PANES];
        fetcher = new FetcherManagerAccounts();
        fetcher.fetch(mainFrame.getUser().getID());

        String[][] topAccounts = ((FetcherManagerAccounts)fetcher).getTopLoans();

        for (String[] info : topAccounts) {
            String desc = info[0];
            String balance = info[1];
            String currency = info[2];

            addNewAccountPanel(desc, balance, currency);
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

    private void loadTransactions() {
        fetcherTransactions = new FetcherManagerTxn();
        fetcherTransactions.fetch(mainFrame.getUser().getID());
        String[][] data = Adapter.parseDailyTxn(fetcherTransactions.getDailyTxn());
        updateTransactionsTable(data);
    }

    public void updateAccounts() {
        loadAccounts();
        resetAccountsPanel();
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    private void resetAccountsPanel() {
        accountsPanel.removeAll();

        for (int i = 0; i < numAccountsShown; i++) {
            accountsPanel.add(accountsList[i]);
        }

        if (numAccountsShown == 0) {
            JLabel noAccounts = new JLabel("No clients have made loan accounts.");
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
        JLabel title = new JLabel("TOP LOANS");
        title.setFont(TextStyle.SUB_HEADING_2_FONT);
        title.setForeground(Pallete.DARK_GREY);
        
        titleHolder.setOpaque(false);
        titleHolder.setLayout(new BorderLayout());
        titleHolder.add(title, BorderLayout.WEST);

        accountsPanel = new JPanel();
        accountsPanel.setLayout(new GridLayout(MAX_ACCOUNT_PANES, 1, 0, margin));
        accountsPanel.setBackground(Color.BLUE);
        accountsPanel.setBorder(BorderFactory.createEmptyBorder(margin, 0, 0, 0));
        accountsPanel.setOpaque(false);

        panel.add(titleHolder, BorderLayout.NORTH);
        panel.add(accountsPanel, BorderLayout.CENTER);
        
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
        JLabel title = new JLabel("Today's Transactions");
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
            noTxnContainer = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            noTxnContainer.setOpaque(false);
            noTxnContainer.setBorder(BorderFactory.createEmptyBorder(marginsY, 0, 0, 0));
            noTxnContainer.add(noTransactions);
            
            tableWrapper.add(noTxnContainer, BorderLayout.CENTER);
        } else {
            if (noTxnContainer != null) {
                tableWrapper.remove(noTxnContainer);
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
            // new Insets(padding, 0, 0, 0), 
            // new Insets(padding, padding, 0, 0)
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
        
        addStocksButton = new RoundedButton("Open New Stock");
        editStocksButton = new RoundedButton("Update Stock Price");
        //removeStocksButton = new RoundedButton("Remove Stock");
        //viewLoansButton = new RoundedButton("View Loans");
        mainActionButtons = new RoundedButton[] {addStocksButton, editStocksButton, 
            //removeStocksButton, viewLoansButton
        };

        int buttonWidth = (int) (1.5 * 8);
        buttonWidth = (int)(screenSize.width / (10 * 8) * buttonWidth);
        int buttonHeight = (int)(screenSize.width / (10 * 8) * 5);

        for (RoundedButton button : mainActionButtons) {
            button.setColor(Pallete.PALE_ORANGE);
            button.setHoverColor(Pallete.PALE_ORANGE_HOVER);
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        }

        InstanceManager commManager = mainFrame.getCommManager();
        addStocksButton.addActionListener(e -> openForm(
            new FormNewStock(commManager)
            ));
        editStocksButton.addActionListener(e -> openForm(
            new FormModStock(commManager)
            ));
        // removeStocksButton.addActionListener(e -> openForm(
        //     null
        //     ));
        // viewLoansButton.addActionListener(e -> openForm(
        //     null
        //     ));
    }
    
}

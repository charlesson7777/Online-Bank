 /*
 * PanelClientTransactions.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Transactions page for a client
 */
package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Communication.Adapter;
import Communication.FetcherTransactions;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.TextStyle;

public class PanelClientTransactions extends PanelClient {
    private MinimalTable transactionsTable;
    private FetcherTransactions fetcherTransactions;
    public PanelClientTransactions(SidenavUserClient nav, OnlineBankClient mainFrame) {
        super(nav, mainFrame);
        initSidenav(nav, mainFrame);
        loadActivity();
    }

    // default is non-vip
    public PanelClientTransactions(OnlineBankClient mainFrame) {
        this(new SidenavUserClient(mainFrame), mainFrame);
    }

    public void loadActivity() {
        initDefaultActivityPanel(MyStrings.TRANSACTIONS_TITLE);
        setActivityPanel();
        loadTransactions();
        super.initialize();
    }

    private void loadTransactions() {
        fetcherTransactions = new FetcherTransactions();
        fetcherTransactions.fetch(mainFrame.getUser().getID());
        String[][] data = Adapter.parseDailyTxn(fetcherTransactions.getAllTxn());
        updateTransactionsTable(data);
    }

    public void updateTransactionsTable(String[][] transactions) {
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

    protected void initSidenav(SidenavUserClient nav, OnlineBankClient mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getTransactionsNav(), true);
    }

    protected void setActivityPanel() {
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);

        transactionsTable = new MinimalTable(screenSize, MyStrings.TRANSACTIONS_HEADERS.length, Color.WHITE);
        transactionsTable.setHeaders(MyStrings.TRANSACTIONS_HEADERS, Color.WHITE, Pallete.DARK_GREY, true);
        transactionsTable.setRowColors(Color.WHITE, Color.WHITE, Pallete.DARK_GREY);
        transactionsTable.setBorderStyle(false);

        MinimalScrollPane scrollPane = new MinimalScrollPane(transactionsTable, Color.WHITE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, marginX));
        activityPanel.add(scrollPane, BorderLayout.CENTER);

    }

}


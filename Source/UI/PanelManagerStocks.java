 /*
 * PanelClientStockMarket.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Stock market page for a client
 */
package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Communication.Adapter;
import Communication.FetcherStocksMarket;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.TextStyle;

public class PanelManagerStocks extends PanelManager {
    private MinimalTable stocksTable;
    private FetcherStocksMarket fetcherStocks;

    public PanelManagerStocks(SidenavManager nav, OnlineBankManager mainFrame) {
        super(nav, mainFrame);
        initSidenav(nav, mainFrame);
        fetcherStocks = new FetcherStocksMarket();
        loadActivity();
    }

    public PanelManagerStocks(OnlineBankManager mainFrame) {
        this(new SidenavManager(mainFrame), mainFrame);
    }

    public void loadActivity() {
        initDefaultActivityPanel(MyStrings.STOCKS_TITLE);
        setActivityPanel();
        loadStocks();
        super.initialize();
        
    }

    public void loadStocks() {
        fetcherStocks.fetch(mainFrame.getUser().getID());
        String[][] data = Adapter.parseStockListing(fetcherStocks.getStocks());
        updateStocksTable(data);
    }

    private void updateStocksTable(String[][] data) {
        if (data == null) {
            JLabel noTransactions = new JLabel("No stocks in the market.");
            noTransactions.setFont(TextStyle.BODY_FONT);
            noTransactions.setForeground(Pallete.LIGHT_GREY);

            int marginX = (int) (screenSize.width / ((10.0 * 8) / 4));
            JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            container.setOpaque(false);
            container.setBorder(BorderFactory.createEmptyBorder(0, marginX, 0, 0));
            container.add(noTransactions);
            
            activityPanel.add(container, BorderLayout.CENTER);
        } else {
            stocksTable.setData(data);
        }

    }

    protected void initSidenav(SidenavManager nav, OnlineBankManager mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(nav.getStocksNav(), true);
    }

    protected void setActivityPanel() {
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);

        stocksTable = new MinimalTable(screenSize, MyStrings.STOCK_MARKET_HEADERS.length, Color.WHITE);
        stocksTable.setHeaders(MyStrings.STOCK_MARKET_HEADERS, Color.WHITE, Pallete.DARK_GREY, true);
        stocksTable.setRowColors(Color.WHITE, Color.WHITE, Pallete.DARK_GREY);
        stocksTable.setBorderStyle(false);

        MinimalScrollPane scrollPane = new MinimalScrollPane(stocksTable, Color.WHITE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, marginX));
        activityPanel.add(scrollPane, BorderLayout.CENTER);

    }

}


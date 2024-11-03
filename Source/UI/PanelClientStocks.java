 /*
 * PanelClientStocks.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Client stocks page that shows stocks clients own
 */
package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BankObjects.StockOwned;
import Communication.Adapter;
import Communication.FetcherStockOwned;
import UI.Forms.FormBuyStock;
import UI.Forms.FormSellStock;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.TextStyle;

public class PanelClientStocks extends PanelClient {
    private MinimalTable stocksTable;

    public PanelClientStocks(SidenavUserClientVIP nav, OnlineBankClientVIP mainFrame) {
        super(nav, mainFrame);
        initSidenav(nav, mainFrame);
        fetcher = new FetcherStockOwned();
        loadActivity();
    }

    public PanelClientStocks(OnlineBankClientVIP mainFrame) {
        this(new SidenavUserClientVIP(mainFrame), mainFrame);
    }

    public void loadActivity() {
        initDefaultActivityPanel(MyStrings.STOCKS_TITLE);
        setActivityPanel();
        updateStocks();
        super.initialize();
    }

    public void updateStocks() {
        fetcher.fetch(mainFrame.getUser().getID());
        List<StockOwned> myStocks = ((FetcherStockOwned)fetcher).getStocks();
        String[][] data = Adapter.parseStockOwned(myStocks);
        updateTable(data);
    }

    private void updateTable(String[][] data) {
        if (data == null) {
            JLabel noTransactions = new JLabel("No stocks purchased.");
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

    protected void initSidenav(SidenavUserClient nav, OnlineBankClientVIP mainFrame) {
        super.initSidenav(nav, mainFrame);
        nav.setActive(((SidenavUserClientVIP)nav).getStocksNav(), true);
    }

    protected void setActivityPanel() {
        int marginX = screenSize.width / ((10 * 8) / 4);
        int marginY = screenSize.height / ((10 * 8) / 4);

        // title
        JPanel titleContainer = activityPanel.getTitleContainer();
        int padding = screenSize.width / ((10 * 8) / 1);
        activityPanel.getTitle().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, padding));

        // plus buttons
        RoundedButton newStocksButton = new RoundedButton("+");
        newStocksButton.setForeground(Pallete.MEDIUM_GREY);
        newStocksButton.setFont(TextStyle.SUB_HEADING_2_FONT);
        newStocksButton.setColor(Pallete.PALE_BLUE);
        newStocksButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        newStocksButton.setBorderPainted(false);
        newStocksButton.addActionListener(e -> openForm(
            new FormBuyStock(mainFrame.getCommManager(), mainFrame.getUser())));
        titleContainer.add(newStocksButton);

        // close account button
        RoundedButton sellStocksbutton = new RoundedButton("-");
        sellStocksbutton.setForeground(Pallete.MEDIUM_GREY);
        sellStocksbutton.setFont(TextStyle.SUB_HEADING_2_FONT);
        sellStocksbutton.setColor(Pallete.PALE_BLUE);
        sellStocksbutton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        sellStocksbutton.setBorderPainted(false);
        sellStocksbutton.addActionListener(e -> openForm(
            new FormSellStock(mainFrame.getCommManager(), mainFrame.getUser(), (FetcherStockOwned)fetcher)
        ));
        titleContainer.add(sellStocksbutton);

        stocksTable = new MinimalTable(screenSize, MyStrings.STOCKS_HEADERS.length, Color.WHITE);
        stocksTable.setHeaders(MyStrings.STOCKS_HEADERS, Color.WHITE, Pallete.DARK_GREY, true);
        stocksTable.setRowColors(Color.WHITE, Color.WHITE, Pallete.DARK_GREY);
        stocksTable.setBorderStyle(false);

        MinimalScrollPane scrollPane = new MinimalScrollPane(stocksTable, Color.WHITE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, marginX, marginY, marginX));
        activityPanel.add(scrollPane, BorderLayout.CENTER);

    }

}


 /*
 * PanelAccountsHolder.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * A UI element used to contain bank accounts
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BankObjects.TypeAccount;
import UI.Theme.MinimalScrollPane;
import UI.Theme.MinimalTable;
import UI.Theme.MyStrings;
import UI.Theme.Pallete;

public class PanelAccountsHolder extends JPanel {
    protected TypeAccount type;
    protected MinimalTable table;
    protected int maxHeight;
    protected int maxWidth;
    protected int marginsX;
    protected int marginsY;
    protected int height;
    
    public PanelAccountsHolder(Dimension screenSize, String name) {
        type = TypeAccount.parseType(name);
        maxWidth = (int) (screenSize.width  / ((10) / 6.85));
        maxHeight = screenSize.height  / (int) ((5.5) / 2.4);
        setOpaque(false);

        marginsX = (int) (screenSize.width  / ((10 * 8) / 1));
        marginsY = screenSize.height  / (int) ((5.5 * 8) / 1);
    
        setLayout(new BorderLayout());
    
        // add margins
        JPanel marginsWrapper = new JPanel();
        marginsWrapper.setLayout(new BoxLayout(marginsWrapper, BoxLayout.Y_AXIS)); 
        marginsWrapper.setBorder(BorderFactory.createEmptyBorder(marginsX, marginsY, marginsX, marginsY/2));
        marginsWrapper.setOpaque(false);

        // setup table scroll
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, marginsY/2));
        MinimalScrollPane scrollPane = new MinimalScrollPane(tableContainer, Pallete.PALE_BLUE, Pallete.PALE_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
        // create table
        setTable(screenSize, name);
        

        // add padding between table and scroll
        tableContainer.add(table, BorderLayout.CENTER);
        tableContainer.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        
        // add to screen
        marginsWrapper.add(table.getTableHeader(), BorderLayout.NORTH);
        marginsWrapper.add(scrollPane, BorderLayout.CENTER);
        marginsWrapper.setOpaque(false);
        add(marginsWrapper);
        updateSize();
    }

    private void setTable(Dimension screenSize, String name) {

        if (name.toUpperCase().equals(TypeAccount.LOAN.toString())) {
            int[] alignment = new int[] {JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT};
            table = new MinimalTable(screenSize, MyStrings.ACCOUNTS_HEADERS.length, Pallete.PALE_BLUE);
            table.setHeaders(MyStrings.LOANS_ACCOUNT_HEADERS, Pallete.PALE_BLUE, Pallete.MEDIUM_GREY, true);
            table.setHeadersPadding(0, 0);
            table.setHeaderAlignment(alignment);
            table.setRowColors(Pallete.PALE_BLUE, Pallete.PALE_BLUE, Pallete.MEDIUM_GREY);
            table.setRowAlignment(alignment);
            table.setBorderStyle(true);
            int tableWidth = maxWidth - (marginsY * 3);
            int[] colWidths = new int[] {(int)(tableWidth * 3.0 / 5.0), (int)(tableWidth * 0.5 / 5.0), (int)(tableWidth * 1.0 / 5.0), (int)(tableWidth * 0.5 / 5.0)};
            table.setColumnWidths(colWidths);
            table.setRowPadding(0, 0);
        } else {
            int[] alignment = new int[] {JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT};
            table = new MinimalTable(screenSize, MyStrings.ACCOUNTS_HEADERS.length, Pallete.PALE_BLUE);
            table.setHeaders(MyStrings.ACCOUNTS_HEADERS, Pallete.PALE_BLUE, Pallete.MEDIUM_GREY, true);
            table.setHeadersPadding(0, 0);
            table.setHeaderAlignment(alignment);
            table.setRowColors(Pallete.PALE_BLUE, Pallete.PALE_BLUE, Pallete.MEDIUM_GREY);
            table.setRowAlignment(alignment);
            table.setBorderStyle(true);
            int tableWidth = maxWidth - (marginsY * 3);
            int[] colWidths = new int[] {(int)(tableWidth * 3.5 / 5.0), (int)(tableWidth * 1.0 / 5.0), (int)(tableWidth * 0.5 / 5.0)};
            table.setColumnWidths(colWidths);
            table.setRowPadding(0, 0);
        }
        
    }

    public TypeAccount getAccountType() {
        return type;
    }

    // used to change table sizes depending on the number of rows
    public void updateSize() {
        int dynamicHeight = table.getTableHeader().getPreferredSize().height + (table.getRowHeight() * (table.getRowCount()+1) + (int)(marginsY/3));
        dynamicHeight = Math.min(dynamicHeight, maxHeight);
        this.height = dynamicHeight;
        setPreferredSize(new Dimension(maxWidth, dynamicHeight));
        revalidate(); 
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int arc = 30;
        g2.setColor(Pallete.PALE_BLUE);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, arc, arc));
        g2.dispose();
    }

    public MinimalTable getTable() {
        return table;
    }

}


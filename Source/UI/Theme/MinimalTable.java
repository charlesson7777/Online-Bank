 /*
 * MinimalTable.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Setup a unified theme and structure for all tables
 */
package UI.Theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MinimalTable extends JTable {
    private int rowPadding;
    private int rowHeight;

    public MinimalTable(Dimension screenSize, int headerLength, Color tableColor) {
        super(new DefaultTableModel(0, headerLength) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        this.rowPadding = screenSize.width / (int)((10 * 8) / 0.5);
        this.rowHeight = screenSize.height / (int)((10 * 8) / 3);
        setRowSelectionAllowed(false);
        setRowHeight(rowHeight);
        setGridColor(tableColor);
        setBackground(tableColor);
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(false);
    }

    public void setHeadersPadding(int paddingX, int paddingY) {

        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                JLabel label = (JLabel) renderer;
                label.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, rowPadding, paddingY));
                return label;
            }
        });
    }

    public void setHeaders(String[] headers, Color headerColor, Color textColor, boolean bold) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setColumnIdentifiers(headers);

        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                JLabel label = (JLabel) renderer;
                label.setFont(bold ? TextStyle.BODY_BOLD : TextStyle.BODY_FONT);
                label.setBorder(BorderFactory.createEmptyBorder(rowPadding, rowPadding, rowPadding, rowPadding));
                label.setForeground(textColor);
                label.setBackground(headerColor);
                return label;
            }
        });
    }

    public void setHeaderAlignment(int[] alignment) {
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) renderer).setHorizontalAlignment(alignment[column]);
                return renderer;
            }
        });
    }

    
    
    public void setRowAlignment(int[] alignment) {
        for (int i = 0; i < alignment.length; i++) {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(alignment[i]);
            getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
    
    
    

    public void setRowPadding(int marginX, int marginY) {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

        setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JLabel) {
                    Border padding = BorderFactory.createEmptyBorder(marginY, marginX, marginY, marginX);
                    ((JLabel) component).setBorder(padding);
                }

                return component;
            }
        });
    }


    public void setDataColumnBold(int colNum, boolean bold) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setFont(bold ? TextStyle.BODY_BOLD : TextStyle.BODY_FONT);
        getColumnModel().getColumn(colNum).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component rendererComponent = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (rendererComponent instanceof JLabel) {
                    ((JLabel) rendererComponent).setFont(bold ? TextStyle.BODY_BOLD : TextStyle.BODY_FONT);
                }
                return rendererComponent;
            }
        });
    }


    public void setRowColors(Color evenRowColor, Color oddRowColor, Color textColor) {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(row % 2 == 0 ? evenRowColor : oddRowColor);
                if (component instanceof JLabel) {
                    ((JLabel) component).setForeground(textColor);
                }
                return component;
            }
        };

        setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JLabel) {
                    Border padding = BorderFactory.createEmptyBorder(rowPadding, rowPadding, rowPadding, rowPadding);
                    Border topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Pallete.PALE_GREY);

                    Border compoundBorder = BorderFactory.createCompoundBorder(topBorder, padding);

                    ((JLabel) component).setBorder(compoundBorder);
                }

                return component;
            }
        });
    }

    public void setColumnWidths(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    public void setBorderStyle(boolean noBorders) {
        if (noBorders) {
            setBorder(null); 
        } else {
            Border topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Pallete.PALE_GREY); 
            setBorder(topBorder);
        }
    }

    public void setData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0); 
        for (Object[] rowData : data) {
            model.addRow(rowData);
        }
        repaint();
    }
    

    public void addData(Object[] rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(rowData);
        repaint();
    }

    public void addData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        for (Object[] rowData : data) {
            model.addRow(rowData);
            repaint();
        }
    }
}


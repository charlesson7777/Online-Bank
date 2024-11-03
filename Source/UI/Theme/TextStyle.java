 /*
 * TextStyle.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Set up a unified theme in font, set up hierarchies for 
 * relaying text information
 */
package UI.Theme;

import javax.swing.UIManager;
import java.awt.Font;

public class TextStyle {
    public static final int SUB_BODY_FONT_SIZE = 11;
    public static final int BODY_FONT_SIZE = 18;
    public static final int HEADING_FONT_SIZE = 40;
    public static final int SUB_HEADING_1_FONT_SIZE = 28;
    public static final int SUB_HEADING_2_FONT_SIZE = 24;
    public static final String THEME_FONT = "Helvetica";
    public static final Font HEADING_FONT = loadFontFromTheme(THEME_FONT, Font.BOLD, HEADING_FONT_SIZE);
    public static final Font SUB_HEADING_1_FONT = loadFontFromTheme(THEME_FONT, Font.PLAIN, SUB_HEADING_1_FONT_SIZE);
    public static final Font SUB_HEADING_1_BOLD = loadFontFromTheme(THEME_FONT, Font.BOLD, SUB_HEADING_1_FONT_SIZE);
    public static final Font SUB_HEADING_2_FONT = loadFontFromTheme(THEME_FONT, Font.PLAIN, SUB_HEADING_2_FONT_SIZE);
    public static final Font SUB_HEADING_2_BOLD = loadFontFromTheme(THEME_FONT, Font.BOLD, SUB_HEADING_2_FONT_SIZE);
    public static final Font BODY_FONT = loadFontFromTheme(THEME_FONT, Font.PLAIN, BODY_FONT_SIZE);
    public static final Font BODY_BOLD = loadFontFromTheme(THEME_FONT, Font.BOLD, BODY_FONT_SIZE);
    public static final Font SUB_BODY_FONT = loadFontFromTheme(THEME_FONT, Font.PLAIN, SUB_BODY_FONT_SIZE);

    public static void setDefaultFont() {
        Font defaultFont = BODY_FONT;
        UIManager.put("Button.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("List.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuBar.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("RadioButton.font", defaultFont);
        UIManager.put("ScrollPane.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("TextPane.font", defaultFont);
        UIManager.put("TitledBorder.font", defaultFont);
        UIManager.put("ToggleButton.font", defaultFont);
        UIManager.put("ToolTip.font", defaultFont);
        UIManager.put("Tree.font", defaultFont);

    }

    private static Font loadFontFromTheme(String fontName, int style, int size) {
        return new Font(fontName, style, size);
    }
    
}

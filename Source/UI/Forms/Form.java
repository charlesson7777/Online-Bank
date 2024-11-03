 /*
 * Form.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Setup a unified theme and structure for all forms 
 */
package UI.Forms;

import javax.swing.*;

import Communication.FormValidation;
import Communication.InstanceManager;
import UI.Theme.MinimalTextField;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.TextStyle;

import java.awt.*;

public abstract class Form extends JFrame {
    protected JLabel title;
    protected JPanel formBody;
    protected RoundedButton submitButton;
    protected Dimension screenSize;
    protected FormValidation validation;
    protected InstanceManager manager;

    public Form(String title, InstanceManager m) {
        super(title);
        manager = m;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        initializeComponents();
    }

    private void initializeComponents() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 3);
        int width = (int)(screenSize.width * 0.35);
        int height = (int) (screenSize.height * 0.90);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        title = new JLabel("Form");
        title.setFont(TextStyle.SUB_HEADING_1_BOLD);
        title.setForeground(Pallete.DARK_GREY);
        title.setBorder(BorderFactory.createEmptyBorder(margins, margins, 0, margins));
        add(title, BorderLayout.NORTH);

        formBody = new JPanel(new GridLayout(15, 1, 0, margins/8));
        formBody.setOpaque(false);
        formBody.setBorder(BorderFactory.createEmptyBorder(margins/2, margins, 0, margins));
        add(formBody, BorderLayout.CENTER);

        

        submitButton = new RoundedButton("Submit");
        submitButton.setColor(Pallete.PALE_BLUE);
        submitButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        submitButton.setFont(TextStyle.BODY_FONT);
        submitButton.setBorder(BorderFactory.createEmptyBorder(margins/4, margins/4, margins/4, margins/4));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(margins, margins, margins, margins));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public RoundedButton getButton() {
        return submitButton;
    }

    public void setTitle(String name) {
        title.setText(name);
    }

    public JPanel getFormBody() {
        return formBody;
    }

    public void addFormPrompt(String prompt) {
        JLabel labelText1 = new JLabel(prompt);
        labelText1.setFont(TextStyle.BODY_FONT);
        formBody.add(labelText1);
    }

    public JTextField addTextField() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        MinimalTextField textField = new MinimalTextField();
        textField.setFont(TextStyle.BODY_FONT);
        formBody.add(textField);
        formBody.add(Box.createVerticalStrut(margins));
        return textField;
    }


}

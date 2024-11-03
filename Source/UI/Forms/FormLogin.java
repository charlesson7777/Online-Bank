 /*
 * FormLogin.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for login
 */
package UI.Forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Communication.FormValidationLogin;
import Communication.FormValidationRegistration;
import Communication.InstanceManager;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.RoundedToggleButton;
import UI.Theme.TextStyle;

public class FormLogin extends Form {
    private RoundedToggleButton registrationButton, signInButton;
    protected JTextField usernameSignIn, passwordSignIn;

    // sign up information 
    protected JTextField firstname, lastname;
    protected JTextField usernameSignUp, passwordSignUp, passwordReconfirm;

    public FormLogin(InstanceManager manager) {
        super("Login", manager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
    }

    private void initialize() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 3);
        
        // title
        title = new JLabel("Sign In / Register");
        title.setFont(TextStyle.SUB_HEADING_1_BOLD);
        title.setForeground(Pallete.DARK_GREY);
        title.setBorder(BorderFactory.createEmptyBorder(margins, margins, 0, margins));
        add(title, BorderLayout.NORTH);
        
        // add card layout buttons
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(margins/2, margins, 0, margins));
        bodyPanel.setOpaque(false);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, margins/4, 0));
        buttonsPanel.setOpaque(false);

        registrationButton = new RoundedToggleButton("Registration");
        registrationButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        registrationButton.setBackground(Pallete.PALE_BLUE);
        registrationButton.setSelected(Pallete.PALE_BLUE_HOVER);
        registrationButton.addActionListener(e -> {
            if (registrationButton.isSelected()) {
                signInButton.setSelected(false);
                signInButton.setForeground(Pallete.LIGHT_GREY);
                registrationButton.setForeground(Pallete.DARK_GREY);
                setupRegistration();
            }
        });

        signInButton = new RoundedToggleButton("Sign In");
        signInButton.setBackground(Pallete.PALE_BLUE);
        signInButton.setSelected(Pallete.PALE_BLUE_HOVER);
        signInButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        signInButton.setSelected(true);    
        registrationButton.setForeground(Pallete.LIGHT_GREY);
        signInButton.addActionListener(e -> {
            if (signInButton.isSelected()) {
                registrationButton.setSelected(false);
                registrationButton.setForeground(Pallete.LIGHT_GREY);
                signInButton.setForeground(Pallete.DARK_GREY);
                setupSignIn();
            }
        });

        buttonsPanel.add(signInButton);
        buttonsPanel.add(registrationButton);
        bodyPanel.add(buttonsPanel, BorderLayout.NORTH);
        
        // form body (edited by functions below)
        formBody = new JPanel(new GridLayout(15, 1, 0, margins/8));
        formBody.setOpaque(false);
        formBody.setBorder(BorderFactory.createEmptyBorder(margins/4, 0, 0, 0));
        bodyPanel.add(formBody, BorderLayout.CENTER);
        
        add(bodyPanel, BorderLayout.CENTER);
        
        // submit button
        submitButton = new RoundedButton("Submit");
        submitButton.setColor(Pallete.PALE_BLUE);
        submitButton.setHoverColor(Pallete.PALE_BLUE_HOVER);
        submitButton.setFont(TextStyle.BODY_FONT);
        submitButton.setBorder(BorderFactory.createEmptyBorder(margins/4, margins/4, margins/4, margins/4));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(margins, margins, margins, margins));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // start with request as default
        setupSignIn();
    }
    

    private void setupSignIn() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();
        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }

        registrationButton.setSelected(false);
        registrationButton.setForeground(Pallete.LIGHT_GREY);
        signInButton.setForeground(Pallete.DARK_GREY);
    
        addFormPrompt("Enter username:");
        usernameSignIn = addTextField();
    
        addFormPrompt("Enter password:");
        passwordSignIn = addTextField();
    
        submitButton.addActionListener(e -> processSignIn());
    }
    

    private void setupRegistration() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();
        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }
        
        addFormPrompt("Username:");
        usernameSignUp = addTextField();

        addFormPrompt("First name:");
        firstname = addTextField();

        addFormPrompt("Last name:");
        lastname = addTextField();

        addFormPrompt("Password:");
        passwordSignUp = addTextField();

        addFormPrompt("Re-confirm password:");
        passwordReconfirm = addTextField();

        submitButton.addActionListener(e -> processRegistration());

        JOptionPane.showMessageDialog(this, "Spaces will be trimmed from all fields. Usernames are NOT case sensitive.",
                     "Warning", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processRegistration() {
        
        validation = new FormValidationRegistration(manager);
        String fname = firstname.getText().trim().isEmpty() ? "" : firstname.getText().trim();
        String lname = lastname.getText().trim().isEmpty() ? "" : lastname.getText().trim();
        String registerUsername = usernameSignUp.getText().trim().isEmpty() ? "" : usernameSignUp.getText().trim();
        String registerPassword = passwordSignUp.getText().trim().isEmpty() ? "" : passwordSignUp.getText().trim();
        String registerPasswordCheck = passwordReconfirm.getText().trim().isEmpty() ? "" : passwordReconfirm.getText().trim();
    
        boolean success = ((FormValidationRegistration)validation).process(
            new String[] {fname, lname, registerUsername, registerPassword, registerPasswordCheck});

        if (success) {
            setupSignIn();
            JOptionPane.showMessageDialog(this, 
                "Please login to your newly registered account.", "Registration success", JOptionPane.INFORMATION_MESSAGE);
                
        }
    }
    

    private void processSignIn() {
        
        validation = new FormValidationLogin(manager);
        String username = usernameSignIn.getText().trim();
        String password = passwordSignIn.getText().trim();
        ((FormValidationLogin)validation).process(new String[] {username, password});

    }

    public void closeForm() {
        setVisible(false);
        dispose(); 
    }
    
}

package UI.Forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BankObjects.BankAccount;
import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.RoundedToggleButton;
import UI.Theme.TextStyle;

public class FormNewAccountVIP extends FormNewAccount {
    private FetcherAccounts fetcher;
    private RoundedToggleButton normalAccButton, vipAccButton;
    private List<BankAccount> accList;
    private JComboBox<String> withdrawAccountCombo;

    public FormNewAccountVIP(InstanceManager manager, User user, FetcherAccounts f) {
        super(manager, user);
        initialize();
        fetcher = f;
    }

    @Override
    protected void initialize() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 3);
        
        // title
        title = new JLabel("Create New Account");
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

        normalAccButton = new RoundedToggleButton("Savings / Checking");
        vipAccButton = new RoundedToggleButton("Securities");

        normalAccButton.setBackground(Pallete.PALE_BLUE);
        normalAccButton.setSelected(Pallete.PALE_BLUE_HOVER);
        normalAccButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        normalAccButton.setSelected(true);    
        vipAccButton.setForeground(Pallete.LIGHT_GREY);
        normalAccButton.addActionListener(e -> {
            if (normalAccButton.isSelected()) {
                vipAccButton.setSelected(false);
                vipAccButton.setForeground(Pallete.LIGHT_GREY);
                normalAccButton.setForeground(Pallete.DARK_GREY);
                setupNormal();
            }
        });

        
        vipAccButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        vipAccButton.setBackground(Pallete.PALE_BLUE);
        vipAccButton.setSelected(Pallete.PALE_BLUE_HOVER);
        vipAccButton.addActionListener(e -> {
            if (vipAccButton.isSelected()) {
                normalAccButton.setSelected(false);
                normalAccButton.setForeground(Pallete.LIGHT_GREY);
                vipAccButton.setForeground(Pallete.DARK_GREY);
                setupVIP();
            }
        });

        buttonsPanel.add(normalAccButton);
        buttonsPanel.add(vipAccButton);
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
        setupNormal();
    }

    private void setupNormal() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();

        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }
    
        addFormPrompt("Select account type:");
        typeCombo = new MinimalDropDown<>(TypeAccount.getAccountStrArr(user.getPrivilege()));
        formBody.add(typeCombo);
        int marginsY = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(marginsY));

        addFormPrompt("Enter starting deposit amount:");
        amountField = addTextField();

        addFormPrompt("Select currency:");

        String[] currencies = TypeCurrency.getCurrencyStrArr();
        currencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(currencyCombo);
    
        submitButton.addActionListener(e -> process());

    }

    private void setupVIP() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);

        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }

        addFormPrompt("Select account to withdraw from:");
        withdrawAccountCombo = new MinimalDropDown<>(fetchAccounts());
        formBody.add(withdrawAccountCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Enter starting deposit amount:");
        amountField = addTextField();

        addFormPrompt("Select currency:");

        String[] currencies = TypeCurrency.getCurrencyStrArr();
        currencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(currencyCombo);

        submitButton.addActionListener(e -> processSecurities());
    }

    private void processSecurities() {
        String senderId = String.valueOf(accList.get(withdrawAccountCombo.getSelectedIndex()).getID());
        String accountType = TypeAccount.SECURITIES.toString();
        String amount = amountField.getText().trim().isEmpty() ? "" : amountField.getText().trim();
        String currency = (String) currencyCombo.getSelectedItem();
        
        boolean success = validation.process(new String[] {accountType, senderId, amount, currency});

        if (success) {
            this.dispose();
        }
    }

    private String[] fetchAccounts() {
        String[] toReturn;

        accList = fetcher.getNonLoanAccounts();
        toReturn = new String[accList.size()];
        for (int i = 0; i < accList.size(); i++) {
            toReturn[i] = accList.get(i).getAccountType() + " - " + 
            String.valueOf(accList.get(i).getID()).substring(12) + 
            " (" + accList.get(i).getCurrency().getName() + ")";
        }
        
        return toReturn;
    }
    
}

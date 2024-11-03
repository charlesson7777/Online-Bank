 /*
 * FormLoans.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Form for requesting and paying loans
 */
package UI.Forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import BankObjects.BankAccount;
import BankObjects.BankAccountLoan;
import BankObjects.TypeAccount;
import BankObjects.TypeCollateral;
import BankObjects.TypeCurrency;
import BankObjects.User;
import Communication.FetcherAccounts;
import Communication.FormValidationLoansPay;
import Communication.FormValidationLoansReq;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;
import UI.Theme.Pallete;
import UI.Theme.RoundedButton;
import UI.Theme.RoundedToggleButton;
import UI.Theme.TextStyle;

public class FormLoans extends Form {
    // buttons to switch forms
    private RoundedToggleButton loanRequestButton, loanPaymentButton;
    
    // loan payment fields
    protected JTextField paymentAmountField;
    protected JComboBox<String> withdrawAccountCombo, loanAccountCombo;
    private List<BankAccountLoan> loanAccList;
    private List<BankAccount> accList;
    private FetcherAccounts fetcher;
    
    // loan request fields
    protected JTextField requestAmountField, collateralValue;
    protected JComboBox<String> collateralCombo, requestCurrencyCombo, collateralCurrencyCombo;
    
    protected User user;

    public FormLoans(InstanceManager manager, User user, FetcherAccounts fetcher) {
        super("Manage Loans", manager);
        this.user = user;
        this.fetcher = fetcher;
        initialize();
    }

    private void initialize() {
        int margins = (int)(screenSize.width / (10.0 * 8) * 3);
        
        // title
        title = new JLabel("Manage Loans");
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

        loanPaymentButton = new RoundedToggleButton("Make Loan Payment");
        loanPaymentButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        loanPaymentButton.setBackground(Pallete.PALE_BLUE);
        loanPaymentButton.setSelected(Pallete.PALE_BLUE_HOVER);
        loanPaymentButton.addActionListener(e -> {
            if (loanPaymentButton.isSelected()) {
                loanRequestButton.setSelected(false);
                loanRequestButton.setForeground(Pallete.LIGHT_GREY);
                loanPaymentButton.setForeground(Pallete.DARK_GREY);
                setUpLoanPayment();
            }
        });

        loanRequestButton = new RoundedToggleButton("Request Loan");
        loanRequestButton.setBackground(Pallete.PALE_BLUE);
        loanRequestButton.setSelected(Pallete.PALE_BLUE_HOVER);
        loanRequestButton.setBorder(BorderFactory.createEmptyBorder(margins/6, margins/6, margins/6, margins/6));
        loanRequestButton.setSelected(true);    
        loanPaymentButton.setForeground(Pallete.LIGHT_GREY);
        loanRequestButton.addActionListener(e -> {
            if (loanRequestButton.isSelected()) {
                loanPaymentButton.setSelected(false);
                loanPaymentButton.setForeground(Pallete.LIGHT_GREY);
                loanRequestButton.setForeground(Pallete.DARK_GREY);
                setupLoanRequest();
            }
        });

        buttonsPanel.add(loanRequestButton);
        buttonsPanel.add(loanPaymentButton);
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
        setupLoanRequest();
    }
    

    private void setupLoanRequest() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();

        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }

        
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
    
        addFormPrompt("Amount requested:");
        requestAmountField = addTextField();
    
        addFormPrompt("Select loan currency:");
        String[] currencies = TypeCurrency.getCurrencyStrArr();
        requestCurrencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(requestCurrencyCombo);
        formBody.add(Box.createVerticalStrut(margins));
    
        addFormPrompt("Select a collateral type:");
        collateralCombo = new MinimalDropDown<>(getCollaterals());
        formBody.add(collateralCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Enter collateral values:");
        collateralValue = addTextField();

        addFormPrompt("Select collateral value currency:");
        collateralCurrencyCombo = new MinimalDropDown<>(currencies);
        formBody.add(collateralCurrencyCombo);
    
        submitButton.addActionListener(e -> processLoanRequest());
    }

    
    private String[] getCollaterals() {
        String[] toReturn = new String[TypeCollateral.values().length];
        int i = 0;
        for (TypeCollateral value : TypeCollateral.values()) {
            toReturn[i++] = value.toString();
        }

        return toReturn;
    }

    private void setUpLoanPayment() {
        formBody.removeAll();
        formBody.revalidate();
        formBody.repaint();
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);

        for (ActionListener listener : submitButton.getActionListeners()) {
            submitButton.removeActionListener(listener);
        }

        addFormPrompt("Select account to withdraw from:");
        withdrawAccountCombo = new MinimalDropDown<>(fetchAccounts(null));
        formBody.add(withdrawAccountCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Select loan account to pay");
        loanAccountCombo = new MinimalDropDown<>(fetchAccounts(TypeAccount.LOAN));
        formBody.add(loanAccountCombo);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Payment amount (in loan account currency):");
        paymentAmountField = addTextField();

        submitButton.addActionListener(e -> processLoanPayment());
    }

    private String[] fetchAccounts(TypeAccount type) {
        String[] toReturn;

        if (type == null) {
            accList = fetcher.getNonLoanAccounts();
            toReturn = new String[accList.size()];
            for (int i = 0; i < accList.size(); i++) {
                toReturn[i] = accList.get(i).getAccountType() + " - " + 
                String.valueOf(accList.get(i).getID()).substring(12) + 
                " (" + accList.get(i).getCurrency().getName() + ")";
            }
        } else {
            loanAccList = fetcher.getLoanAccounts();
            toReturn = new String[loanAccList.size()];
            for (int i = 0; i < loanAccList.size(); i++) {
                toReturn[i] = "Ending in " + 
                String.valueOf(loanAccList.get(i).getID()).substring(12) + 
                " (" + round(loanAccList.get(i).getBalance(), 2) + " " + 
                loanAccList.get(i).getCurrency().getName() + ")";
            }
        }
        
        return toReturn;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void processLoanPayment() {
        validation = new FormValidationLoansPay(manager, user);

        String senderId = String.valueOf(accList.get(withdrawAccountCombo.getSelectedIndex()).getID());
        String loanAccountId = String.valueOf(loanAccList.get(loanAccountCombo.getSelectedIndex()).getID());
        String amount = paymentAmountField.getText();
        
        boolean success = validation.process(new String[] {senderId, loanAccountId, amount});
        if (success) {
            this.dispose();
        }
        
    }

    private void processLoanRequest() {
        validation = new FormValidationLoansReq(manager, user);
        String amountStr = requestAmountField.getText();
        String currency = (String) requestCurrencyCombo.getSelectedItem();
        String collateralType = collateralCombo.getSelectedItem().toString();
        String collateralVal = collateralValue.getText().toString();
        String collateralCurrency = collateralCurrencyCombo.getSelectedItem().toString();

        boolean success = validation.process(new String[] {amountStr, currency, collateralType, collateralVal, collateralCurrency});

        if (success) {
            this.dispose();
        }

    }
}

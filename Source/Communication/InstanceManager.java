 /*
 * InstanceManager.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Manages the UI flow from one screen to another 
 * Allows communication between different JFrames
 */
package Communication;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.SwingUtilities;

import BankObjects.BankAccount;
import BankObjects.TypeAccount;
import BankObjects.TypeCurrency;
import BankObjects.User;
import BankObjects.UserClient;
import BankObjects.UserClientVIP;
import BankObjects.UserManager;
import BankObjects.UserPrivilege;
import UI.OnlineBank;
import UI.OnlineBankClient;
import UI.OnlineBankClientVIP;
import UI.OnlineBankManager;
import UI.PanelClient;
import UI.PanelClientAccounts;
import UI.PanelClientHome;
import UI.PanelClientStocks;
import UI.Forms.FormLogin;
import UI.Theme.TextStyle;

public class InstanceManager {
    private FormLogin loginForm; 
    private OnlineBank bank;

    public InstanceManager() {
        TextStyle.setDefaultFont();
        launch();
    }

    public void launch() {
        login();
    }

    public void login() {
        loginForm = new FormLogin(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginForm.setVisible(true);
            }
        });
    }

    private void initializeNormalClient(User user) {  
        bank = new OnlineBankClient(user, this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bank.initializeMainFrame();
                bank.setName(user.getFirstName(), user.getLastName());
                bank.loadInitialPanel();
            }
        });
    }

    private void initializeVIPClient(User user) {
        bank = new OnlineBankClientVIP(user, this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ((OnlineBankClientVIP)bank).initializeMainFrame();
                ((OnlineBankClientVIP)bank).setName(user.getFirstName(), user.getLastName());
                ((OnlineBankClientVIP)bank).loadInitialPanel();
            }
        });
    }

    private void initializeManager(User user) {
        bank = new OnlineBankManager((UserManager)user, this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ((OnlineBankManager)bank).initializeMainFrame();
                ((OnlineBankManager)bank).loadInitialPanel();
            }
        });
    }

    public void updateAccounts(UserPrivilege privilege) {
        PanelClient panel;
        switch (privilege) {
            case NORMAL:
                panel = ((OnlineBankClient)bank).getActivePanel();
                if (panel instanceof PanelClientAccounts) {
                    ((PanelClientAccounts)panel).updateAccounts();
                } else if (panel instanceof PanelClientHome) {
                    ((PanelClientHome)panel).updateAccounts();
                }
                break;

            case VIP:
                panel = ((OnlineBankClientVIP)bank).getActivePanel();
                if (panel instanceof PanelClientAccounts) {
                    ((PanelClientAccounts)panel).updateAccounts();
                } else if (panel instanceof PanelClientHome) {
                    ((PanelClientHome)panel).updateAccounts();
                }
                break;
            case MANAGER:
                break;
            default:
                break;
        }
    }

    public void updateBalance() {
        PanelClient panel = ((OnlineBankClient)bank).getActivePanel();
        if (panel instanceof PanelClientHome) {
            ((PanelClientHome)panel).updateAccounts();
            ((PanelClientHome)panel).updateTransactions();
        }

        
    }

    public void logout() {
        // delete reference to old bank
        bank.close();
        bank = null;
        login();
    }


    public void onLoginSuccess(User user) {
        if (loginForm != null) {
            loginForm.closeForm();
            loginForm = null; 
            
            switch(user.getPrivilege()) {
                case NORMAL:
                    initializeNormalClient(user);
                    break;
                case VIP: 
                    initializeVIPClient(user);
                    break;
                case MANAGER:
                    initializeManager(user);
                    break; 
                default: 
                    break;

            }

        }

    }

    public void updateStocks() {
        PanelClient panel = ((OnlineBankClient)bank).getActivePanel();
        ((PanelClientStocks)panel).updateStocks();
    }

    public User checkUserPrivilege(User user) {
        DB db = DB.getDB();
        User updatedUser = user;
        if (user.getPrivilege() == UserPrivilege.VIP) {
            double totalSavings = 0;
            for (BankAccount account : db.getAllAccountsOfUser(user.getID())) {
                if (account.getAccountType() == TypeAccount.SAVING) {
                    // from account currency to USD
                    double exchangeRate = db.getExchangeRate(account.getCurrency().getId(), TypeCurrency.USD.getId());
                    totalSavings += account.getBalance() * exchangeRate;
                }
            }

            if (round(totalSavings, 2) < UserPrivilege.MIN_VIP_AMOUNT_MAINTAIN) {
                db.updateUserPrivilege(user.getID(), UserPrivilege.NORMAL);
                updatedUser = new UserClient(user.getID());
            }

        } else if (user.getPrivilege() == UserPrivilege.NORMAL) {
            double totalSavings = 0;
            for (BankAccount account : db.getAllAccountsOfUser(user.getID())) {
                if (account.getAccountType() == TypeAccount.SAVING) {
                    // from account currency to USD
                    double exchangeRate = db.getExchangeRate(account.getCurrency().getId(), TypeCurrency.USD.getId());
                    totalSavings += account.getBalance() * exchangeRate;
                }
            }

            if (round(totalSavings, 2) >= UserPrivilege.MIN_VIP_AMOUNT_CREATION) {
                db.updateUserPrivilege(user.getID(), UserPrivilege.VIP);
                updatedUser = new UserClientVIP(user.getID());
            }

        }
        
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());

        return updatedUser;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    
}

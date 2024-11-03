//This class is being used for testing the functionality of the database operations related to user authentication, account management, and stock transactions.

import Communication.DB;
import Communication.DBManager;

import java.util.UUID;

import BankObjects.BANCreator;
import BankObjects.BankAccount;
import BankObjects.BankAccountSecurities;
import BankObjects.Stock;
import BankObjects.StockOwned;
import BankObjects.User;
import BankObjects.UserManager;
import BankObjects.UserPrivilege;
import BankObjects.BankAccount.*;

public class DBtesting {
    public static void main(String[] args) {
        DB db = DB.getDB();
        DBManager dbm = DBManager.getDBManager();
        // User user = new UserManager(UUID.randomUUID());
        // user.setUsername("manager");
        // user.setPassword("123456789");
        // user.setFirstName("admin");
        // user.setLastName("account");
        // db.addNewUser(user);
        // User user = db.loginValidation("testtest", "123456789");
        // BankAccount account = new BankAccountSecurities(user.getID());
        // account.setBalance(10000.00);
        // account.setCurrency(db.getCurrencyById(1));
        // account.setAccountNumber(3123354265239673l);
        // db.addNewAccount(user.getID(), account);
        // Stock stock = db.fetchAllStocks().get(0);
        // StockOwned so = new StockOwned(UUID.randomUUID(), account.getID(), stock.getPrice(), stock.getName(), 20, db.getCurrencyById(1), stock.getID());
        // db.purchaseStock(so);
        // UUID sid = db.getAllStockOwnedByAccount(account.getID()).get(0).getSid();
        // Stock stock = db.fetchStockById(sid);
        System.out.println(dbm.getDailyTransaction().get(0).getDate());
    }
}

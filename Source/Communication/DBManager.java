package Communication;


/*
 * DBManager.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Contains all manager methods required to communicate with our sql database 
 */

package Communication;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import BankObjects.*;

public class DBManager {
    // environment variables for cloud db connection, I know there might be some safety issues here, but this is the simplest way.
    private String dbHost = "aws.connect.psdb.cloud";
   String dbUsername = System.getenv("DB_USERNAME");  // Use environment variables

    String dbPassword = System.getenv("DB_PASSWORD");  
    private String dbName = "611-final";
    private String url = "jdbc:mysql://" + dbHost + "/" + dbName;
    private Properties props;
    private DB db;
    // use singleton pattern here to ensure there is only one db instance.
    private static DBManager dbmanager;
    private DBManager() {
        // initialize jdbc connection props
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", dbUsername);
        connectionProps.setProperty("password", dbPassword);
        connectionProps.setProperty("useSSL", "true"); // Enable SSL

        this.props = connectionProps;
        this.db = DB.getDB();
    }
    public static DBManager getDBManager() {
        if (dbmanager == null) {
            dbmanager = new DBManager();
        }
        return dbmanager;
    }

    // methods

    /*
     * ------------------------------User-----------------------------
     */

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = null;
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                // check the privilege of user to return different instance
                switch (UserPrivilege.valueOf(resultSet.getString("privilege"))) {
                    case NORMAL:
                        user = new UserClient(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;

                    case VIP:
                        user = new UserClientVIP(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;

                    case MANAGER:
                        user = new UserManager(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;
                
                    default:
                        break;
                }
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return users;
    }

    public User getUserByID(UUID uid) {
        User user = null;
        String query = "SELECT * FROM user WHERE iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                switch (UserPrivilege.valueOf(resultSet.getString("privilege"))) {
                    case NORMAL:
                        user = new UserClient(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;

                    case VIP:
                        user = new UserClientVIP(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;

                    case MANAGER:
                        user = new UserManager(uid);
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        break;
                
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return user;
    }



    /*
     * ------------------------------Account-----------------------------
     */
    public List<BankAccount> getAllaccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        String query = "SELECT * FROM account";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                Long idaccount = Long.valueOf(resultSet.getString("idaccount"));
                String accountType = resultSet.getString("type");
                double balance = resultSet.getDouble("balance");
                Currency currency = db.getCurrencyById(resultSet.getInt("idcurrency"));

                // add different account instance to list according to the type of account
                BankAccount account = null;
                switch (TypeAccount.valueOf(accountType)) {
                    case CHECKING:
                        account = new BankAccountChecking(uid);
                        break;

                    case SAVING:
                        account = new BankAccountSaving(uid);
                        break;
                    
                    case SECURITIES:
                        account = new BankAccountSecurities(uid);
                        break;
                
                    default:
                        break;
                }

                // set the attri
                account.setAccountNumber(idaccount);
                account.setBalance(balance);
                account.setCurrency(currency);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public List<BankAccountLoan> getAllLoans() {
        List<BankAccountLoan> accounts = new ArrayList<>();
        String query = "SELECT * FROM loan";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                Long idaccount = Long.valueOf(resultSet.getString("idloan"));
                double balance = resultSet.getDouble("amount");
                Currency currency = db.getCurrencyById(resultSet.getInt("idcurrency"));

                BankAccountLoan account = new BankAccountLoan(uid);
                account.setAccountNumber(idaccount);
                account.setBalance(balance);
                account.setCurrency(currency);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }


    /*
     * ------------------------------Stock-----------------------------
     */

    // add stock to DB
    public Boolean addNewStock(Stock stock) {
        String query = "INSERT INTO stock (idstock, code, name, price, idcurrency, numofshare) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the params
            preparedStatement.setString(1, stock.getID().toString());
            preparedStatement.setString(2, stock.getCode());
            preparedStatement.setString(3, stock.getName());
            preparedStatement.setDouble(4, stock.getPrice());
            preparedStatement.setInt(5, stock.getCurrency().getId());
            preparedStatement.setInt(6, stock.getNumShares());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // update the price of a stock
    public Boolean updateStockPrice(UUID sid, double newPrice) {
        String query = "UPDATE stock SET price = ? WHERE idstock = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setString(2, sid.toString());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * ------------------------------Transaction-----------------------------
     */

    // get daily transaction
    public List<Transaction> getDailyTransaction() {
        List<Transaction> dailyTransactions = new ArrayList<>();

        String query = "SELECT * FROM transaction WHERE DATE(date) = CURDATE()";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                UUID tid = UUID.fromString(resultSet.getString("idtransaction"));
                long aid1 = Long.parseLong(resultSet.getString("idaccount1"));
                long aid2 = 0l;
                if(resultSet.getString("idaccount2") != null) {
                    aid2 = Long.parseLong(resultSet.getString("idaccount2"));
                }
                Currency currency = db.getCurrencyById(resultSet.getInt("idcurrency"));
                double amount = resultSet.getDouble("amount");
                String transType = resultSet.getString("type");
                Date date = new Date(resultSet.getTimestamp("date").getTime());
                // there's a column for description in the database, but we do not use it for now.

                // create different transaction instance base on the type
                Transaction transaction = null;
                switch (TypeTransaction.valueOf(transType)) {
                    case WITHDRAWAL:
                        transaction = new TransactionWithdrawal(amount, currency, aid1, tid, uid);
                        transaction.setDate(date);
                        dailyTransactions.add(transaction);
                        break;
                        
                    case DEPOSIT:
                        transaction = new TransactionDeposit(amount, currency, aid1, tid, uid);
                        transaction.setDate(date);
                        dailyTransactions.add(transaction);
                        break;

                    case TRANSFER:
                        transaction = new TransactionTransfer(amount, currency, aid1, aid2, tid, uid);
                        transaction.setDate(date);
                        dailyTransactions.add(transaction);
                        break;
                
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dailyTransactions;
    }
}

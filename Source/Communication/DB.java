package Communication;


/*
 * DB.java
 * Kathlyn F. Sinaga (kathlyn@bu.edu)
 * Heyang (heyung) Yu (jhyyu@bu.edu)
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Contains all client methods required to communicate with our sql database 
 */


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import BankObjects.*;

public class DB {
    // environment variables for cloud db connection, I know there might be some safety issues here, but this is the simplest way.
    private String dbHost = "aws.connect.psdb.cloud";
    private String dbUsername = System.getenv("DB_USERNAME");  // Use environment variables

    private String dbPassword = System.getenv("DB_PASSWORD");  // Use environment variables

    private String dbName = "611-final";
    private String url = "jdbc:mysql://" + dbHost + "/" + dbName;
    private Properties props;
    private static double MONTH_INTEREST = 0.003;

    // use singleton pattern here to ensure there is only one db instance.
    private static DB db;
    private DB() {
        // initialize jdbc connection props
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", dbUsername);
        connectionProps.setProperty("password", dbPassword);
        connectionProps.setProperty("useSSL", "true"); // Enable SSL

        this.props = connectionProps;
    }
    public static DB getDB() {
        if (db == null) {
            db = new DB();
        }
        return db;
    }

    // methods

    /*
     * ------------------------------USER-----------------------------
     */

    // validate the username and password, return the user instance if login successfully
    public User loginValidation(String username, String password) {
        User user = null;

        // define the query
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if successfully find the user 
            if (resultSet.next()) {
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
                // get month interval
                if(resultSet.getTimestamp("lastlogin") != null) {
                    // get monthcnt
                    int monthCnt = getMonthInterval(uid);
                    // System.out.println(monthCnt);
                    // add interest to all saving accounts according to the month count
                    // charge interest for all loans
                    if(monthCnt > 0) {
                        if(user.getPrivilege() == UserPrivilege.VIP) addInterestToAllSavingAccounts(uid, monthCnt);
                        chargeInterestForAllLoans(uid, monthCnt);
                    }
                }
                // set last login
                updateLastLogin(uid);
                // System.out.println("updated timestamp");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // add new user to database, return true if successfully added a user
    public boolean addNewUser(User user) {
        String query = "INSERT INTO user (iduser, username, password, privilege, email, tel, firstname, lastname, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the params
            preparedStatement.setString(1, user.getID().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getPrivilege().toString());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getFirstName());
            preparedStatement.setString(8, user.getLastName());
            preparedStatement.setString(9, user.getAddress());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get the time (by month) between current time and last login, return -1 if error or no login hist
    public int getMonthInterval(UUID uid) {
        String query = "SELECT TIMESTAMPDIFF(MONTH, lastlogin, CURRENT_DATE()) AS month_difference FROM user WHERE iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int monthDifference = resultSet.getInt("month_difference");
                return monthDifference;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    // update privilege of a user, return true if updated successfully
    public boolean updateUserPrivilege(UUID uid, UserPrivilege privilege) {
        String query = "UPDATE user SET privilege = ? WHERE iduser = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, privilege.toString());
            preparedStatement.setString(2, uid.toString());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // update the last login timestamp for user
    public boolean updateLastLogin(UUID uid) {
        String query = "UPDATE user SET lastlogin = CURRENT_TIMESTAMP() WHERE iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());

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
     * ------------------------------CURRENCY-----------------------------
     */

    // get currency by currency id
    public Currency getCurrencyById(int currencyId) {
        Currency currency = null;
        String query = "SELECT * FROM currency WHERE idcurrency = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, currencyId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String sign = resultSet.getString("sign");
                String name = resultSet.getString("name");
                currency = new Currency(currencyId, sign, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }
    
    /*
     * ------------------------------ACCOUNT-----------------------------
     */
    
    // get all accounts of a user
    public List<BankAccount> getAllAccountsOfUser(UUID uid) {
        String uid_str = uid.toString();
        List<BankAccount> accountList = new ArrayList<>();

        // query
        String query = "SELECT * FROM account WHERE iduser = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid_str);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long idaccount = Long.valueOf(resultSet.getString("idaccount"));
                String accountType = resultSet.getString("type");
                double balance = resultSet.getDouble("balance");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));

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
                accountList.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // return account list
        return accountList;
    }

    // get account by id
    public BankAccount getAccountById(long aid) {
        BankAccount account = null;

        String query = "SELECT * FROM account WHERE idaccount = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Long.toString(aid));
            
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                String accountType = resultSet.getString("type");
                double balance = resultSet.getDouble("balance");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));

                // add different account instance to list according to the type of account
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
                account.setAccountNumber(aid);
                account.setBalance(balance);
                account.setCurrency(currency);
            }
        } catch (SQLException e) {
            account = null;
            //e.printStackTrace();
        }

        return account;
    }

    // add an account to database
    public boolean addNewAccount(UUID uid, BankAccount account) {
        String query = "INSERT INTO account (idaccount, iduser, type, balance, idcurrency) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Long.toString(account.getID()));
            preparedStatement.setString(2, uid.toString());
            preparedStatement.setString(3, account.getAccountType().toString());
            preparedStatement.setDouble(4, account.getBalance());
            preparedStatement.setInt(5, account.getCurrency().getId());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // set the balance of an account, return true if successfully updated
    public boolean updateAccountBalance(long aid, double newBalance) {
        String query = "UPDATE account SET balance = ? WHERE idaccount = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, Long.toString(aid));

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete an account, return true if deleted successfully
    public boolean deleteAccount(UUID uid, long aid) {
        String query = "DELETE FROM account WHERE iduser = ? AND idaccount = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());
            preparedStatement.setString(2, Long.toString(aid));
            
            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get all saving accounts
    public List<BankAccount> getAllSavingAccountOfUser(UUID uid) {
        List<BankAccount> accountList = getAllAccountsOfUser(uid);
        List<BankAccount> savingAccounts = new ArrayList<>();
        for(BankAccount account: accountList) {
            if(account instanceof BankAccountSaving) savingAccounts.add(account);
        }
        return savingAccounts;
    }

    // add interest to all saving account
    public void addInterestToAllSavingAccounts(UUID uid, int monthCnt) {
        for(BankAccount account: getAllSavingAccountOfUser(uid)) {
            double balance = account.getBalance();
            double newBalance = balance * Math.pow(1 + MONTH_INTEREST, monthCnt);
            if(updateAccountBalance(account.getID(), newBalance)){
                continue;
            } else {
                System.out.println("Error accumulating interest!");
            }
        }
    }
    
    /*
     * ------------------------------Transaction-----------------------------
     */


    // add new transaction to database
    public boolean addNewTransactionRecord(Transaction transaction) {
        String query = "INSERT INTO transaction (idtransaction, iduser, idaccount1, idaccount2, idcurrency, amount, type, date, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, transaction.getID().toString());
            preparedStatement.setString(2, transaction.getUserId().toString());
            switch (transaction.getType()) {
                case DEPOSIT:
                    preparedStatement.setString(3, Long.toString(((TransactionDeposit)transaction).getAccountID()));
                    preparedStatement.setString(4, null);
                    break;

                case WITHDRAWAL:
                    preparedStatement.setString(3, Long.toString(((TransactionWithdrawal)transaction).getAccountID()));
                    preparedStatement.setString(4, null);
                    break;

                case TRANSFER:
                    preparedStatement.setString(3, Long.toString(((TransactionTransfer)transaction).getSenderID()));
                    preparedStatement.setString(4, Long.toString(((TransactionTransfer)transaction).getRecipientID()));
                    break;
            
                default:
                    break;
            }
            preparedStatement.setInt(5, transaction.getCurrency().getId());
            preparedStatement.setDouble(6, transaction.getAmount());
            preparedStatement.setString(7, transaction.getType().toString());
            preparedStatement.setTimestamp(8, new Timestamp(transaction.getTimestamp()));
            preparedStatement.setString(9, "test");

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get trasactions of a user, return in descending order of time
    public List<Transaction> getAllTransactionsOfUser(UUID uid) {
        List<Transaction> transList = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE iduser = ? ORDER BY date DESC";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                UUID tid = UUID.fromString(resultSet.getString("idtransaction"));
                long aid1 = Long.parseLong(resultSet.getString("idaccount1"));
                long aid2 = 0l;
                if(resultSet.getString("idaccount2") != null) {
                    aid2 = Long.parseLong(resultSet.getString("idaccount2"));
                }
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
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
                        transList.add(transaction);
                        break;
                        
                    case DEPOSIT:
                        transaction = new TransactionDeposit(amount, currency, aid1, tid, uid);
                        transaction.setDate(date);
                        transList.add(transaction);
                        break;

                    case TRANSFER:
                        transaction = new TransactionTransfer(amount, currency, aid1, aid2, tid, uid);
                        transaction.setDate(date);
                        transList.add(transaction);
                        break;
                
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transList;
    }

    // execute a transaction, we always need to check if account 1 is valid,
    // then for transfer and withdrawal, we will need to check if the account have enough balance, 
    // and for transfer we need to check if the target user is valid.
    // return true if the transaction is executed successfully
    public boolean executeTransaction(Transaction transaction) {
        switch (transaction.getType()) {
            case DEPOSIT:
                // for deposit we only need the account 1 to be an existing account
                BankAccount accountDeposit = getAccountById(((TransactionDeposit)transaction).getAccountID());
                if(accountDeposit == null) {
                    System.out.println("Invalid account!");
                    return false;
                }
                // update the balance
                double newBalanceDeposit = accountDeposit.getBalance() + transaction.getAmount();
                if(updateAccountBalance(accountDeposit.getID(), newBalanceDeposit)) return true;
                else return false;

            case WITHDRAWAL:
                // first check if account 1 exists
                BankAccount accountWithdrawal = getAccountById(((TransactionWithdrawal)transaction).getAccountID());
                if(accountWithdrawal == null) {
                    System.out.println("Invalid account!");
                    return false;
                }
                // check if the account have enough balance
                if(accountWithdrawal.getBalance() < transaction.getAmount()) {
                    System.out.println("Balance not enough!");
                    return false;
                } 
                // execute the withdrawal
                double newBalanceWithdrawal = accountWithdrawal.getBalance() - transaction.getAmount();
                if(updateAccountBalance(accountWithdrawal.getID(), newBalanceWithdrawal)) return true;
                else return false;

            case TRANSFER:
                // first check if any of these two account does not exist
                BankAccount sender = getAccountById(((TransactionTransfer)transaction).getSenderID());
                BankAccount recipient = getAccountById(((TransactionTransfer)transaction).getRecipientID());
                if(sender == null || recipient == null) {
                    System.out.println("Invalid sender or recipient");
                    return false;
                }
                // cannot transfer to the same account
                if(sender.getID() == recipient.getID()) {
                    System.out.println("You cannot transfer to the same account!");
                    return false;
                }
                // check if sender has enough balance
                if(sender.getBalance() < transaction.getAmount()) {
                    System.out.println("This sender doesn't have enough balance!");
                    return false;
                }
                // execute the transfer
                // TODO: potential conversion
                if(updateAccountBalance(sender.getID(), sender.getBalance() - transaction.getAmount()) &&
                    updateAccountBalance(recipient.getID(), recipient.getBalance() + transaction.getAmount())) {
                        return true;
                } else return false;
            default:
                return false;
        }
    }

    /*
     * ------------------------------Exchange Rate-----------------------------
     */

    public double getExchangeRate(int idcurrency1, int idcurrency2) {
        double exchangeRate = 1;
        String query = "SELECT * FROM exchangerate WHERE idcurrency1= ? AND idcurrency2 = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idcurrency1);
            preparedStatement.setInt(2, idcurrency2);
            
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                exchangeRate = resultSet.getDouble("rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    /*
     * ------------------------------Stock-----------------------------
     */

    // get all stocks in the database
    public List<Stock> fetchAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String query = "SELECT * FROM stock";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID sid = UUID.fromString(resultSet.getString("idstock"));
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                double price = resultSet.getDouble("price");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                int numofshare = resultSet.getInt("numofshare");

                Stock stock = new Stock(name, numofshare, price, currency);
                stock.setCode(code);
                stock.setSid(sid);
                stocks.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    // fetch stock by id
    public Stock fetchStockById(UUID sid) {
        Stock stock = null;
        String query = "SELECT * FROM stock WHERE idstock = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                int numofshare = resultSet.getInt("numofshare");
                double price = resultSet.getDouble("price");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                stock = new Stock(name, numofshare, price, currency);
                stock.setCode(code);
                stock.setSid(sid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    // update the num of share of a stock, return true if updated successfully
    public boolean updateNumOfShare(UUID sid, int numofshare) {
        String query = "UPDATE stock SET numofshare = ? WHERE idstock = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, numofshare);
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
     * ------------------------------Stock Owned-----------------------------
     */

    // get all stocks owned by an account
    public List<StockOwned> getAllStockOwnedByAccount(long aid) {
        List<StockOwned> soList = new ArrayList<>();
        String query = "SELECT * FROM stockowned WHERE idaccount = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Long.toString(aid));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID soId = UUID.fromString(resultSet.getString("idstockowned"));
                long idaccount = Long.parseLong(resultSet.getString("idaccount"));
                UUID idstock = UUID.fromString(resultSet.getString("idstock"));
                Stock stock = fetchStockById(idstock);
                int numofshare = resultSet.getInt("numofshare");
                double purchasePrice = resultSet.getDouble("purchaseprice");
                double soldprice = resultSet.getDouble("soldprice");

                StockOwned so = new StockOwned(soId, aid, purchasePrice, stock.getName(), numofshare, stock.getCurrency(), idstock);
                if(soldprice != 0.00) so.setSoldPrice(soldprice);
                soList.add(so);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soList;
    }
    

    // purchase a stock, add the stock owned to db and update the numofshare of stock
    public boolean purchaseStock(StockOwned so) {
        String query = "INSERT INTO stockowned (idstockowned, idaccount, idstock, numofshare, purchaseprice, soldprice) VALUES (?, ?, ?, ?, ?, ?)";
        Stock stock = fetchStockById(so.getSid());
        BankAccount account = getAccountById(so.getAccountId());
        // check if there are enough numofshare
        if(stock.getNumShares() < so.getNumOfShare()) {
            System.out.println("No enough shares of stock!");
            return false;
        }
        // check if the balance is enough
        if(account.getBalance() < so.getNumOfShare() * stock.getPrice()) {
            System.out.println("No enough balance!");
            return false;
        }
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the params
            preparedStatement.setString(1, so.getSoId().toString());
            preparedStatement.setString(2, Long.toString(so.getAccountId()));
            preparedStatement.setString(3, so.getSid().toString());
            preparedStatement.setInt(4, so.getNumOfShare());
            preparedStatement.setDouble(5, so.getPurchasePrice());
            preparedStatement.setDouble(6, so.getSoldPrice());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            if(result > 0) {
                // update the numofshare of stock
                updateNumOfShare(so.getSid(), stock.getNumShares() - so.getNumOfShare());
                // deduct money from account
                updateAccountBalance(account.getID(), account.getBalance() - so.getNumOfShare() * stock.getPrice());
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // sell a stock owned and update the numofshare of the stock
    public boolean sellAStockOwned(StockOwned so) {
        Stock stock = fetchStockById(so.getSid());
        BankAccount account = getAccountById(so.getAccountId());

        String query = "UPDATE stockowned SET soldprice = ? WHERE idstockowned = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, stock.getPrice());
            preparedStatement.setString(2, so.getSoId().toString());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            if(result > 0) {
                // update account balance
                updateAccountBalance(account.getID(), account.getBalance() + so.getNumOfShare() * stock.getPrice());
                // update numofshare of stock
                updateNumOfShare(so.getSid(), stock.getNumShares() + so.getNumOfShare());
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * ------------------------------Loan-----------------------------
     */

    // fetch all loans of a user from database
    public List<BankAccountLoan> getAllLoansOfUser(UUID uid) {
        List<BankAccountLoan> loanList = new ArrayList<>();

        String query = "SELECT * FROM loan WHERE iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                long idloan = Long.parseLong(resultSet.getString("idloan"));
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                // amount/balance: negative double
                double amount = resultSet.getDouble("amount");
                Date date = new Date(resultSet.getTimestamp("startdate").getTime());
                Collateral collateral = getCollateralById(UUID.fromString(resultSet.getString("idcollateral")));
                BankAccountLoan loan = new BankAccountLoan(collateral, uid);
                loan.setAccountNumber(idloan);
                loan.setBalance(amount);
                loan.setCurrency(currency);
                loan.setDate(date);
                loanList.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loanList;
    }

    // fetch a loan by its id
    public BankAccountLoan getLoanById(long idloan) {
        BankAccountLoan loan = null;
        String query = "SELECT * FROM loan WHERE idloan = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Long.toString(idloan));

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                UUID uid = UUID.fromString(resultSet.getString("iduser"));
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                // amount/balance: negative double
                double amount = resultSet.getDouble("amount");
                Date date = new Date(resultSet.getTimestamp("startdate").getTime());
                Collateral collateral = getCollateralById(UUID.fromString(resultSet.getString("idcollateral")));
                loan = new BankAccountLoan(collateral, uid);
                loan.setAccountNumber(idloan);
                loan.setBalance(amount);
                loan.setCurrency(currency);
                loan.setDate(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }

    // add a loan to database
    public boolean addALoanToDB(BankAccountLoan loan) {
        String query = "INSERT INTO loan (idloan, iduser, idcurrency, amount, startdate, idcollateral) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the params
            preparedStatement.setString(1, Long.toString(loan.getID()));
            preparedStatement.setString(2, loan.getUserID().toString());
            preparedStatement.setInt(3, loan.getCurrency().getId());
            preparedStatement.setDouble(4, loan.getBalance());
            preparedStatement.setTimestamp(5, new Timestamp(loan.getDate().getTime()));
            preparedStatement.setString(6, loan.getCollateral().getID().toString());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // pay for a loan by an account
    public boolean payForLoan(long idloan, double amount) {
        BankAccountLoan loan = getLoanById(idloan);

        // update the balance of account and loan
        String query = "UPDATE loan SET amount = ? WHERE idloan = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, loan.getBalance() + amount);
            preparedStatement.setString(2, Long.toString(idloan));

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            if(result > 0) {
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete a loan by id
    public boolean deleteALoanById(UUID uid, long idloan) {
        String query = "DELETE FROM loan WHERE iduser = ? AND idloan = ?";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());
            preparedStatement.setString(2, Long.toString(idloan));
            
            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // update loan balance
    public boolean updateLoanBalance(long lid, double newBalance) {
        String query = "UPDATE loan SET amount = ? WHERE idloan = ?";

        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, Long.toString(lid));

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // charge interest for all loans
    public void chargeInterestForAllLoans(UUID uid, int monthCnt) {
        for(BankAccountLoan loan: getAllLoansOfUser(uid)) {
            double balance = loan.getBalance();
            double newBalance = balance * Math.pow(1 + MONTH_INTEREST, monthCnt);
            if(updateLoanBalance(loan.getID(), newBalance)) {
                continue;
            } else {
                System.out.println("Error in updating loan balance!");
            }
        }
    }

    /*
     * ------------------------------Collateral-----------------------------
     */

    // add a collateral to database
    public boolean addCollateralToDB(Collateral collateral) {
        String query = "INSERT INTO collateral (idcollateral, iduser, name, value, idcurrency, info, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // set the params
            preparedStatement.setString(1, collateral.getID().toString());
            preparedStatement.setString(2, collateral.getUserId().toString());
            preparedStatement.setString(3, "testCollateral");
            preparedStatement.setDouble(4, collateral.getValue());
            preparedStatement.setInt(5, collateral.getCurrency().getId());
            preparedStatement.setString(6, "testInfo");
            preparedStatement.setString(7, collateral.getType().toString());

            // Execute the update
            int result = preparedStatement.executeUpdate();

            // Check if the update was successful
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // fetch all collateral of a user
    public List<Collateral> getAllCollateralOfUser(UUID uid) {
        List<Collateral> collaterals = new ArrayList<>();
        String query = "SELECT * FROM collateral WHERE iduser = ?"; 
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid.toString());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                UUID iduser = UUID.fromString(resultSet.getString("iduser"));
                String name = resultSet.getString("name");
                Double value = resultSet.getDouble("value");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                String info = resultSet.getString("info");
                String type = resultSet.getString("type");
                Collateral collateral = new Collateral(iduser, TypeCollateral.valueOf(type), value, currency);
                collaterals.add(collateral);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collaterals;
    }

    // fetch a collateral by id
    public Collateral getCollateralById(UUID idcollateral) {
        Collateral collateral = null;
        String query = "SELECT * FROM collateral WHERE idcollateral = ?"; 
        try (Connection connection = DriverManager.getConnection(url, props); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, idcollateral.toString());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                UUID iduser = UUID.fromString(resultSet.getString("iduser"));
                String name = resultSet.getString("name");
                Double value = resultSet.getDouble("value");
                Currency currency = getCurrencyById(resultSet.getInt("idcurrency"));
                String info = resultSet.getString("info");
                String type = resultSet.getString("type");
                collateral = new Collateral(iduser, TypeCollateral.valueOf(type), value, currency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collateral;
    }

    // TODO: mark a collateral as used, I'm not sure if we need to implement this, now there's no column in db for this
    

    // main for testing
    // public static void main(String[] args) {
    //     DB db = getDB();
    //     // // test user
    //     // User newUser = new UserClient(UUID.randomUUID());
    //     // newUser.setUsername("jeremyhyyu");
    //     // newUser.setPassword("123456");
    //     // newUser.setFirstName("Heyang");
    //     // newUser.setLastName("Yu");
    //     // newUser.setAddress("123 Commonwealth ST");
    //     // newUser.setEmail("jhyyu@bu.edu");
    //     // newUser.setPhoneNumber("(123)123-4567");
    //     // if(db.addNewUser(newUser)) {
    //     //     System.out.println("Successfully created a user!");
    //     //     User loginUser = db.loginValidation("jeremyhyyu", "123456");
    //     //     if(loginUser != null) {
    //     //         System.out.println("Successfully log in!");
    //     //     }
    //     // }

    //     // test account
    //     // User user = db.loginValidation("jeremyhyyu", "123456");
    //     // UUID uid = user.getID();
    //     // Long accountid = 100000000l;
    //     // // TypeAccount type = TypeAccount.CHECKING;
    //     // double balance = 0.00;
    //     // BankAccount account = new BankAccountChecking(uid);
    //     // account.setAccountNumber(accountid);
    //     // account.setBalance(balance);
    //     // if(db.addNewAccount(uid, account)) {
    //     //     System.out.println("Successfully added an account!");
    //     // }
    //     // get all accounts
    //     // List <BankAccount> accountList = db.getAllAccountsOfUser(uid);
    //     // System.out.println(accountList.get(0).getCurrency().getSign());

    //     // transaction test
    //     // User user = db.loginValidation("jeremyhyyu", "123456");
    //     // UUID uid = user.getID();
    //     // Transaction transaction = new TransactionDeposit(100.00, db.getCurrencyById(1), 100000000l, UUID.randomUUID(), uid);
    //     // if(db.addNewTransactionRecord(transaction)) {
    //     //     System.out.println("Successfully added the transaction");
    //     // }
    //     System.out.println(db.getExchangeRate(1, 2));
    // }
}


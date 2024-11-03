 /*
 * Adapter.java
 * Beaudlaire Jeancharles (bmalik@bu.edu)
 * 
 * 5/8/2024
 *
 * Follows the adapter pattern to turn lists of BankObjects into 
 * a type that can be used by the UI tables
 */
package Communication;

import java.util.List;
import java.util.UUID;

import BankObjects.BankAccount;
import BankObjects.BankAccountLoan;
import BankObjects.Stock;
import BankObjects.StockOwned;
import BankObjects.Transaction;
import BankObjects.TransactionDeposit;
import BankObjects.TransactionTransfer;
import BankObjects.TransactionWithdrawal;
import BankObjects.UserClient;

public class Adapter {
    
    public Adapter() {

    }

    public static String[][] parseStockListing(List<Stock> stocksForSale) {
        String[][] toReturn = null;
        
        if (!stocksForSale.isEmpty()) {
            toReturn = new String[stocksForSale.size()][5];

            for (int i = 0; i < stocksForSale.size(); i++) {
                Stock stock = stocksForSale.get(i);
                String symbol = stock.getCode();
                symbol = symbol.substring(0, 1).toUpperCase() + symbol.substring(1).toLowerCase();
                String numShares = Integer.toString(stock.getNumShares());
                String price = Double.toString(stock.getPrice());
                String currency = stock.getCurrency().toString();

                toReturn[i] = new String[] {symbol, numShares, price, currency};
            }

        }

        return toReturn;
    }

    public static String[][] parseDailyTxn(List<Transaction> txnList) {
        String[][] toReturn = null;

        if (!txnList.isEmpty()) {
            toReturn = new String[txnList.size()][5];

            for (int i = 0; i < txnList.size(); i++) {
                Transaction txn = (Transaction)txnList.get(i);
                String type = txn.getType().toString();
                String date = txn.getDate().toString();
                String amount = String.format("%.2f", txn.getAmount());
                if (txn.getAmount() > 0) {
                    amount = "+" + amount;
                }
                
                String spacedAccountNum = "";
                String accountNum = "";

                if (txn instanceof TransactionDeposit) {
                    accountNum = Long.toString(((TransactionDeposit)txn).getAccountID());

                } else if (txn instanceof TransactionTransfer) {
                    if (txn.getAmount() > 0) {
                        accountNum = Long.toString(((TransactionTransfer)txn).getRecipientID());
                    } else {
                        accountNum = Long.toString(((TransactionTransfer)txn).getSenderID());
                    }
                } else if (txn instanceof TransactionWithdrawal) {
                    accountNum = Long.toString(((TransactionWithdrawal)txn).getAccountID());
                }
    
                for (int j = 0; j < accountNum.length(); j++) {
                    spacedAccountNum += accountNum.charAt(j);
                    if ((j + 1) % 4 == 0 && i != accountNum.length() - 1) {
                        spacedAccountNum += " ";
                    }
                }
    
                
                String currency = txn.getCurrency().toString();

                toReturn[i] = new String[] {type, date, spacedAccountNum, amount, currency};
    
            }

        }
        
       return toReturn;
    }

    public static String[][] parseStockOwned(List<StockOwned> myStocks) {
        String[][] parsedStockData = null;
    
        if (!myStocks.isEmpty()) {
            parsedStockData = new String[myStocks.size()][6];
    
            for (int i = 0; i < myStocks.size(); i++) {
                StockOwned stock = myStocks.get(i);
                Stock stockInMarket = null;
                try {
                    stockInMarket = DB.getDB().fetchStockById(stock.getSid());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                String symbol = stockInMarket != null ? stockInMarket.getName() : "";
                String shares = Integer.toString(stock.getNumOfShare());
                String currency = stockInMarket.getCurrency().getName();
                String currentPrice = String.format("%.2f", stockInMarket.getPrice());
                double unrealizedProfit;
                double realizedProfit;
    
                if (stock.getSoldPrice() == 0) {
                    realizedProfit = 0;
                    unrealizedProfit = stockInMarket.getPrice() - stock.getPurchasePrice() * stock.getNumOfShare();
                } else {
                    realizedProfit = (stock.getSoldPrice() - stock.getPurchasePrice()) * stock.getNumOfShare();
                    unrealizedProfit = 0;
                }
    
                String unrealized = String.format("%.2f", unrealizedProfit);
                String realized = String.format("%.2f", realizedProfit);
    
                parsedStockData[i] = new String[] {symbol, shares, currency, currentPrice, realized, unrealized};
            }
        }
    
        return parsedStockData;
    }
    
    
    public String[][] parseClientInfo(List<? extends UserClient> clientsList) {
        String[][] toReturn = new String[clientsList.size()][3];

        for (int i = 0; i <clientsList.size(); i++) {
            UserClient client = (UserClient)clientsList.get(i);
            String name = client.getFirstName().substring(0, 1).toUpperCase() + 
                client.getFirstName().substring(1).toLowerCase() + " " + 
                client.getLastName().substring(0, 1).toUpperCase() + 
                client.getLastName().substring(1).toLowerCase();
            String debt = String.format("%.2f", getDebt(client.getUID()));
            String totalBal = String.format("%.2f", getBalance(client.getUID()));
            toReturn[i] = new String[] {name, debt, totalBal};
            
        }
        
        return toReturn;
    }

    private double getBalance(UUID uid) {
        DB db = DB.getDB();
        double totalBal = 0;

        List<BankAccount> accounts = db.getAllAccountsOfUser(uid);

        for (BankAccount account : accounts) {
            totalBal += account.getBalance();
        }

        return totalBal;

    }

    private double getDebt(UUID uid) {
        DB db = DB.getDB();
        double totalDebt = 0;

        List<BankAccountLoan> loans = db.getAllLoansOfUser(uid);

        for (BankAccountLoan loan : loans) {
            totalDebt += loan.getBalance();
        }

        return totalDebt;
    }

    public static String[][] parseBankAccountList(List<? extends BankAccount> accounts) {
        String[][] toReturn = new String[accounts.size()][3];
        
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount account = (BankAccount)accounts.get(i);
            String accountNum = Long.toString(account.getID());

            String spacedAccountNum = "";
            for (int j = 0; j < accountNum.length(); j++) {
                spacedAccountNum += accountNum.charAt(j);
                if ((j + 1) % 4 == 0 && i != accountNum.length() - 1) {
                    spacedAccountNum += " ";
                }
            }

            String balance = String.format("%.2f", account.getBalance());
            String currency = account.getCurrency().toString();

            if (account instanceof BankAccountLoan) {
                String collateral = ((BankAccountLoan)account).getCollateral().getType().toString();
                collateral = collateral.substring(0, 1).toUpperCase() + collateral.substring(1).toLowerCase();

                toReturn[i] = new String[] {spacedAccountNum, collateral, balance, currency};
            } else {

                toReturn[i] = new String[] {spacedAccountNum, balance, currency};
            }

        }
 
        return toReturn;
    }

}

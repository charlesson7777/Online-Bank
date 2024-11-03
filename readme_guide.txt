Kev’s Guide to Readme.txt or Readme.md
Required sections: Header, Files, Notes, How to compile and run, I/O Example

# CS611-Online Bank Final project
## Name of Assignment
---------------------------------------------------------------------------
Name: Beaudlaire Jeancharles, Kathlyn Sinaga, Heyang You
Email: bmalik@bu.edu, kathlyn@bu.edu, jhyyu@bu.edu
Student ID: U70445938

## Files
---------------------------------------------------------------------------
BankObjects Package:

BanCreator.java: This class generates unique bank account numbers.
BankAccount.java: This class is an abstract class representing a general framework for a bank account.
BankAccountChecking.java: This class extends BankAccount class specifically for checking accounts.
BankAccountFactory.java: This class provides methods to create and retrieve bank accounts.
BankAccountLoan.java: This class extends the BankAccount class specifically for loan accounts.
BankAccountSaving.java:  This class extends the BankAccount class specifically for savings accounts.
BankAccountSecurities.java: This class extends the BankAccount class specifically for securities accounts.
Collateral.java: This class  is designed to represent collateral associated with financial transactions, typically loans.
Currency.java: This class encapsulates the properties of different types of currencies.
Stock.java:   This class represents individual stocks in a financial system, encompassing essential attributes and operations related to stock transactions. 
StockOwned.java: This class is designed to represent individual ownership records of stocks within a financial system. 
Transaction.java: This class is an abstract base class designed to model financial transactions within a banking or financial system.
TransactionDeposit.java: This class extends the abstract Transaction class, specifically for handling deposit transactions within a financial system. 
TransactionFactory.java: This class is an abstract base class designed to model financial transactions within a banking or financial system.
TransactionTransfer.java: This class extends the abstract Transaction class, specifically for handling transfer transactions within a financial system. 
TransactionWithdrawal.java: This class extends the abstract Transaction class, specifically for handling withdrawal transactions within a financial system.  
TypeAccount.java:  This class defines various types of bank accounts and encapsulates properties specific to each type.
TypeCollateral.java: This class defines the different types of collateral that can be used in financial transactions, specifically in contexts like securing loans. 
TypeCurrency.java: This class categorizes different types of currencies and associates them with unique database identifiers.
TypeTransaction.java: This class defines different types of financial transactions that can be processed within a banking or financial system, along with associated transaction-specific constraints like minimum amounts and transaction costs. 
User.java: This class  is an abstract class that defines the fundamental characteristics and functionalities for user management within a banking or financial system.
UserClient.java: This class extends the abstract User class which is designed specifically to represent individual clients within a banking or financial system. 
UserClientVIP.java: This class extends the abstract UserClient class which tailored specifically for VIP clients within a banking or financial system. 
UserManager.java: This class extends abstract User class which tailoring its functionalities to cater specifically to user managers within a financial or banking system. 
UserPrivilege.java: This class classifies different levels of user privileges within a financial or banking system, defining clear distinctions between the access rights and functionalities available to users based on their status.
Withdrawable.java:  This interface class defines a set of functionalities that are essential for classes representing financial accounts or similar entities where funds can be withdrawn or transferred. 

Communication Package:

Adapter.java: This class provides utility methods for parsing lists of transactions and bank accounts into structured, human-readable string arrays. 
DB.java: This class serves as a comprehensive database interface for a Java application, handling all interactions with a cloud-hosted MySQL database. 
DBManager.java: This class serves as an advanced database interface, designed to manage a broad array of operations related to users, bank accounts, stocks, and transactions within a Java application using a MySQL database.
Fetcher.java: This class is designed as an abstract class that serves as a framework for fetching data associated with a specific UUID from a database. 
FetcherAccounts.java: This class extends the abstract Fetcher class, specifically designed to handle the fetching and management of bank account data from a database. 
FetcherStocks.java: This class extends the abstract Fetcher class, It's designed to potentially handle the fetching of stock-related data linked to a specific user identified by a UUID.
FetcherTransactions.java: This class extends the abstract Fetcher class, specifically tailored to handle the retrieval and management of transaction data for a user. 
FormValidation.java: This class is designed following the strategy pattern, allowing for different form validation strategies to be implemented for various types of data entry and processing needs within a Java application. 
FormValidationBuyStock.java: This class extends the FormValidationUser class, specifically handles validation of user inputs related to buying stocks, ensuring that the data entered in the form is appropriate and ready for transaction processes such as 					purchasing shares.
FormValidationClose.java: This class extends the FormValidationUser class. It is specifically designed to validate and process user requests for closing bank accounts. 
FormValidationDeposit.java: This class extends the FormValidationUser class, which handles the validation and processing of user input related to depositing funds into bank accounts. 
FormValidationLoansPay.java: This class extends the FormValidationUser class, which is responsible for validating and processing user input related to paying off loans. 
FormValidationLoansReq.java: This class extends the FormValidationNewAccount class, which handles the validation and processing of user input related to requesting a loan account. 
FormValidationLogin.java: This class extends the FormValidation class, which handles the validation and processing of user login credentials. Upon receiving user input, this class verifies the provided username and password against the database records and 				handles the login process accordingly.
FormValidationModStock.java: This class extends the FormValidationUser class, which manages the validation and processing of modifications to stock-related data. 
FormValidationNewAccount.java: This class extends the FormValidationUser class, which manages the validation and processing of requests to create a new bank account. 
FormValidationNewStock.java: This class extends the FormValidationUser class,  which handles the validation and processing of requests to create a new stock. 
FormValidationRegistration.java: This class extends the FormValidation class,  which handles the validation and processing of user registration requests. 
FormValidationTransfer.java: This class extends the FormValidationUser class,  which handles the validation and processing of transfer requests between bank accounts.
FormValidationUser.java: This class extends the FormValidation class,  which provides functionality specific to user-related form validations. 
FormValidationWithdraw.java: This class extends the FormValidationUser class,  which handles the validation and processing of withdrawal transactions. 
InstanceManager.java: This class serves as a central hub for managing instances and coordinating the user interaction flow within the banking application. 

UI Package:

FormBuyStock.java: This class  represents a form for buying stocks within the user interface of the banking application. 
FormCloseAccount.java: This class  is responsible for providing a user interface to close a bank account. 
FormDeposit.java: This class is responsible for providing a user interface to make a new deposit into a bank account.
FormLoans.java: This class  is a comprehensive UI component designed to facilitate managing loans within a banking system. 
FormLogin.java: This class  provides a user interface for both signing in and registering new accounts within a banking system.
FormLogout.java: This class represents a simple confirmation dialog for logging out of the application. 
FormNewAccount.java: This class  provides a user interface for creating a new bank account. 
FormStocks.java: This class serves as a user interface for managing stocks, allowing users to add new stocks or modify existing ones.
FormTransfer.java: This class  provides a user interface for initiating a transfer transaction between bank accounts. 
FormWithdraw.java: This class provides a user interface for initiating a withdrawal transaction from a bank account.
OnlineBank.java: This abstract class serves as the foundation for creating an online banking application. 
OnlineBankClient.java: This abstract class and provides specific implementations for managing the UI of an online banking client application.
OnlineBankClientVIP.java: This class extends The OnlineBankClient class and provides specific implementations for managing the UI of an online banking client application for VIP users. 
OnlineBankManager.java: This class extends The OnlineBank class and is responsible for managing the user interface (UI) of an online banking application for bank managers. 
PanelAccountsHolder.java: This class is a JPanel that displays account information in a table format. 
PanelActivity.java: This class represents a panel that contains an activity within a user interface.
PanelClient.java: This abstract class serves as the base for creating panels within the client interface of an online banking application. 
PanelClientAccounts.java: This class extends PanelClient, which represents the panel for displaying client accounts within the online banking interface.
PanelClientHome.java: This class serves as the home panel within the online banking interface. 
PanelClientStockMarket.java: This class is designed to display stock market information within the online banking interface.
PanelClientStocks.java: This class is designed to manage and display stock-related information within the online banking interface. 
PanelClientTransactions.java: This class  is responsible for managing and displaying transaction-related information within the online banking interface.
PanelManager.java: This class serves as a foundation for managing panels within the banking application.
PanelManagerClients.java: This class extends the PanelManager abstract class which is responsible for managing client accounts within the banking application. 
PanelManagerHome.java: This class extends the PanelManager abstract class which manages the home panel of the banking application.
PanelManagerStocks.java: This class extends the PanelManager abstract class which is responsible for managing the panel displaying stock transactions in the banking application.
PanelManagerTransactions.java: This class extends the PanelManager abstract class which  manages the panel displaying banking transactions in the application. 
Sidenav.java: This class extends Panel which manages the side navigation panel of the application.
SidenavManager.java: This class extends SideNav class  which manages the specific navigation buttons within the side navigation panel. 
SidenavUserClient.java: This class extends SideNav class  which  provides navigation buttons tailored for user clients, including options for Home, Accounts, and Transactions.
SidenavUserClientVIP.java: This class extends SidenavUserClient class  which adds additional navigation buttons tailored for VIP users, specifically for managing stocks and accessing the stock market.

Temp:
RegistrationForm.java: This class creates a simple GUI registration form using Swing components. It includes fields for personal details such as name, email, address, city, state, username, and password.
DBtesting.java: This class is being used for testing the functionality of the database operations related to user authentication, account management, and stock transactions.
Main.java: This class appears to be the entry point of the application. 
mysql-connect-java-8.3.0.jar: is a Java Archive (JAR) file. This type of file is commonly used in Java programming to package Java classes and related metadata.

Theme Package:

Form.java: This class serves as a template for creating various forms in the UI.
MinimalDropDown.java: This class extends JComboBox  to create a custom-styled dropdown component.
MinimalScrollPane.java: This class is designed to customize the look and behavior of a JScrollPane in Java Swing, making it minimalistic and visually appealing.
MinimalTable.java: This class extends JTable and is tailored for a specific aesthetic and functional approach. 
MinimalTextField.java: This class extends JTextfield to create a custom text field with a minimalist design, specifically featuring an underline rather than a full border. 
MyStrings.java: This class serves as a centralized location for storing string constants used throughout the user interface of your application. 
NoButton.java: This class is a simple customization of JButton designed to create a button that effectively has no visible presence or impact on layout. 
Pallete.java: This class defines a set of color constants that are used throughout your application's user interface. 
RoundedButton.java: This class is a customized JButton designed to enhance the visual appeal of buttons in your application by providing rounded corners and custom color handling for different states like hover and press.
RoundedPanel.java: This class is a customized version of JPanel that features rounded corners, enhancing the aesthetics of your application's user interface. 
RoundedToggleButton.java: This class extends JToggleButton and is customized to feature rounded corners and distinct colors for different states (selected vs. normal). 
TextStyle.java: This class is a utility class that centralizes the font definitions and styling used throughout your application's user interface.




## Citations
--------------------------------------------------------------------------------------------------------------------------------
- https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/grid.html (javaswing layouts)
- https://stackoverflow.com/questions/16373459/java-jscrollbar-design (minimalistic scrollbar design)
- https://www.youtube.com/watch?v=g-mrpCgZQyc&ab_channel=IntactAbode (setting up alignments in JTables)
- https://stackoverflow.com/questions/61779298/remove-arrow-buttons-from-jscrollbar-in-jscrollpane (used for minimalistic scrollbar design)


## Design Decisions 
-----------------------------------------------------------------------------------------------------------------------------------
We separated the Objects into 3 major types: Bank Objects, Communication, and UI. Bank Objects mainly contain attributes that we set using information from the database. Objects under communication deal with mutating objects and directing UI updates. Objects under UI hold UI objects and themes and also UI navigation. 


## Notes 
-----------------------------------------------------------------------------------------------------------------------------------
We can change the exchange rate through the DB. 
The data is stored on cloud, so multiple devices can share the consistent information.
We have a set theme for the UI.


##  This class 
---------------------------------------------------------------------------
https://www.geeksforgeeks.org/arrays-in-java/. I used this source to get a quick refresher on Array Syntax in Java

## How to compile and run
---------------------------------------------------------------------------
Your directions on how to run the code. Ideally should resemble the lines below

1. Navigate to the directory "YourCodeDirectory" after unzipping the files
2. Run the following instructions:

## testing
Go to Source
javac *.java
java -cp .:mysql-connector-j-8.3.0.jar Main

Windows
Setup mysql connector : https://dev.mysql.com/doc/connector-odbc/en/connector-odbc-installation-binary-windows.html
del /s *.class
javac *.java
java -cp .;mysql-connector-j-8.3.0.jar Main


Design
---------------------------------------------------------------------------------------------

Our program likely follows the Model-View-Controller (MVC) architecture. The
separation into UI elements (view), database or business logic (model), and the interaction
between them (controller) is hinted at with classes like the InstanceManager class, which could
act as a part of the controller managing the flow between the UI and the data models.
Model Design
Factory Pattern:
The use of factories is evident in creating UI components or handling specific operations
that are dependent on the application state or user type. This could involve creating different
types of forms or panels based on user privileges or account types. The factory method might
be used in PanelAccountsHolder to generate different table settings based on account types.
Strategy Pattern:
This is utilized in the form validation and communication handling where different
strategies for validation or data fetching are encapsulated in classes that can be interchanged
within the family hierarchy. For example, FormValidation classes act as strategies that are used
dynamically within forms to validate user input based on different criteria.
Singleton Pattern:
Used in database and manager classes such as DB and InstanceManager, ensuring that
only a single instance of the manager is instantiated across the application, which is typical in
handling database connections and session management.
In conclusion, the model design in the program reflects a structured approach to
building a robust and scalable Java Swing application. The design incorporates several key
principles and patterns that facilitate clean separation of concerns, modular architecture, and a
consistent user interface.
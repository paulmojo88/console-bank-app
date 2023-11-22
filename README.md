# Bank Console Application

## Description
This is a console application named `bank-console-app` that simulates a banking system. 
The application is built using Java 17 and Gradle, and it uses PostgreSQL for data storage. 
The application also uses JDBC for database connectivity and Lombok to reduce boilerplate code.
It allows users to perform various banking operations such as viewing accounts, creating a new account, 
replenishing an account, withdrawing from an account, and transferring funds to another account.

## Main Entities
- **Bank**: Represents the bank within the application.
- **Account**: Represents a bank account.
- **User**: Represents a user of the bank.
- **Transaction**: Represents a transaction between accounts.

## Features
1. **View Accounts**: Users can view all their existing accounts.
2. **Create a New Account**: Users can create a new account.
3. **Replenish an Account**: Users can add funds to an existing account.
4. **Withdraw from an Account**: Users can withdraw funds from an existing account.
5. **Transfer Funds**: Users can transfer funds from one account to another.
6. **Exit**: Users can exit the application.

## Tech Stack
- **Java 17**: The application is written in Java 17.
- **Gradle**: Gradle is used as the build tool for this project.
- **PostgreSQL**: PostgreSQL is used as the database for storing data.
- **JDBC**: JDBC is used for connecting the application with the PostgreSQL database.
- **Lombok**: Lombok is used to reduce boilerplate code in the application.

## Setup
To run this project, you need to have Java 17 and Gradle installed on your machine.
1. Clone the repository
2. Create a PostgreSQL database named console-bank-app and run the SQL scripts located in the sql folder 
to create the tables and populate them with some sample data.
3. Create a database.properties file in the console-bank-app\src\main\resources folder and
   fill it with your database connection details.  
The file should have the following format:  
   url=jdbc:postgresql://your-host:your-port/console-bank-app  
   user=your-username  
   password=your-password
4. Navigate to the project directory
5. Run `gradle build` to build the project
6. Run `gradle run` to start the application


## Dependencies
The `AppBank` class depends on the following services:
- `AccountService`: This service handles all account-related operations.
- `UserService`: This service handles all user-related operations.

## Future Enhancements
We are constantly working to improve `AppBank` and make it more user-friendly. 
If you have any suggestions or feedback, please feel free to share.

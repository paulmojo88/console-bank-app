package org.example;

import org.example.data.dto.AccountDto;
import org.example.data.dto.UserDto;
import org.example.data.util.BankAccountNumberGenerator;
import org.example.service.AccountService;
import org.example.service.ServiceFactory;
import org.example.service.UserService;

import java.util.List;
import java.util.Scanner;

public class AppBank {
    private final AccountService accountService;
    private final UserService userService;
    private final Scanner scanner;
    private double amount;

    public AppBank(AccountService accountService, UserService userService, Scanner scanner) {
        this.accountService = accountService;
        this.userService = userService;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AccountService accountService = serviceFactory.getAccountService();
        UserService userService = serviceFactory.getUserService();
        Scanner scanner = new Scanner(System.in);
        AppBank appBank = new AppBank(accountService, userService, scanner);
        appBank.run();
    }
    public void run() {
        System.out.println("Welcome to Bank console app!");
        System.out.println("Please enter your user id:");
        Long userId = scanner.nextLong();
        UserDto user = userService.getUserById(userId);
        if (user != null) {
            System.out.println("Hello, " + user.getUserName() + "!");
            boolean running = true;
            while (running) {
                System.out.println("Please choose an option:");
                System.out.println("1. View your accounts");
                System.out.println("2. Create a new account");
                System.out.println("3. Replenish an account");
                System.out.println("4. Withdraw from an account");
                System.out.println("5. Transfer funds to another account");
                System.out.println("6. Exit");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> viewAccountsInfo(userId);
                    case 2 -> creatNewAccount(userId);
                    case 3 -> replenish(userId);
                    case 4 -> withdraw(userId);
                    case 5 -> transfer(userId);
                    case 6 -> {
                        System.out.println("Thank you for using Bank console app!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("User not found with id: " + userId);
        }
    }

    private void transfer(Long userId) {
        System.out.println("Please enter the sender account id:");
        Long senderAccountId = scanner.nextLong();
        AccountDto senderAccount = accountService.getAccountById(senderAccountId);
        if (senderAccount == null || !senderAccount.getUserId().equals(userId)) {
            System.out.println("Invalid sender account id.");
        } else {
            System.out.println("Please enter the recipient account id:");
            Long recipientAccountId = scanner.nextLong();
            AccountDto recipientAccount = accountService.getAccountById(recipientAccountId);
            if (recipientAccount == null) {
                System.out.println("Invalid recipient account id.");
            } else {
                System.out.println("Please enter the amount to transfer:");
                amount = scanner.nextDouble();
                try {
                    transfer(senderAccount, recipientAccount, amount);
                    System.out.println("You have transferred funds successfully.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void withdraw(Long userId) {
        long accountId;
        System.out.println("Please enter the account id:");
        accountId = scanner.nextLong();
        AccountDto withdrawAccount = accountService.getAccountById(accountId);
        if (withdrawAccount == null || !withdrawAccount.getUserId().equals(userId)) {
            System.out.println("Invalid account id.");
        } else {
            System.out.println("Please enter the amount to withdraw:");
            amount = scanner.nextDouble();
            try {
                withdraw(withdrawAccount, amount);
                System.out.println("You have withdrawn from your account successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void replenish(Long userId) {
        System.out.println("Please enter the account id:");
        long accountId = scanner.nextLong();
        AccountDto repAccount = accountService.getAccountById(accountId);
        if (repAccount == null || !repAccount.getUserId().equals(userId)) {
            System.out.println("Invalid account id.");
        } else {
            System.out.println("Please enter the amount to replenish:");
            double amount = scanner.nextDouble();
            try {
                replenish(repAccount, amount);
                System.out.println("You have replenished your account successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void creatNewAccount(Long userId) {
        System.out.println("Please enter the account type (savings or checking):");
        String accountType = scanner.next();
        System.out.println("Please enter the bank id:");
        Long bankId = scanner.nextLong();
        System.out.println("Please enter the balance amount:");
        Double balance = scanner.nextDouble();
        AccountDto newAccount = new AccountDto();
        String accountNumber = BankAccountNumberGenerator.generateIBAN();
        newAccount.setAccountNumber(accountNumber);
        newAccount.setAccountBalance(balance);
        newAccount.setAccountType(accountType);
        newAccount.setUserId(userId);
        newAccount.setBankId(bankId);
        AccountDto account = accountService.createAccount(newAccount);
        System.out.println("You have created a new account:");
        System.out.println(account);
    }

    private void viewAccountsInfo(Long userId) {
        List<AccountDto> accounts = accountService.getAllByUserId(userId);
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
        } else {
            System.out.println("You have " + accounts.size() + " accounts:");
            for (AccountDto account : accounts) {
                System.out.println(account.showAccountInfo());
            }
        }
    }

    private void transfer(AccountDto senderAccount, AccountDto recipientAccount, double amount) {
        accountService.transfer(senderAccount, recipientAccount, amount);
    }

    private void withdraw(AccountDto account, double amount) {
        accountService.withdraw(account, amount);
    }

    private void replenish(AccountDto account, double amount) {
        accountService.replenish(account, amount);
    }
}
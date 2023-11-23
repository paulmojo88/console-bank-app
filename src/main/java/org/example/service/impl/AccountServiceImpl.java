package org.example.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.data.dao.AccountDao;
import org.example.data.dao.BankDao;
import org.example.data.dao.DaoFactory;
import org.example.data.dao.TransactionDao;
import org.example.data.dto.AccountDto;
import org.example.data.util.ObjectMapper;
import org.example.entity.Account;
import org.example.entity.Bank;
import org.example.entity.Transaction;
import org.example.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final String CONFIG_FILE = "src/main/resources/config.yml";
    private static final String RECEIPT_FOLDER = "src/main/checks";
    private static final String RECEIPT_FORMAT = "check_%s_%s.pdf";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String INTEREST_RATE_KEY = "interest_rate";

    private AccountDao accountDao;
    private BankDao bankDao;
    private TransactionDao transactionDao;
    private Transaction transaction;

    public AccountServiceImpl() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        if (accountDao == null) {
            accountDao = daoFactory.getAccountDao();
        }
        if (bankDao == null) {
            bankDao = daoFactory.getBankDao();
        }
        if (transactionDao == null) {
            transactionDao = daoFactory.getTransactionDao();
        }
        scheduleInterestCalculation();
    }

    public void replenish(AccountDto accountDto, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDao.getById(accountDto.getAccountId());
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        synchronized (account) {
            double oldBalance = account.getAccountBalance();
            double newBalance = oldBalance + amount;
            account.setAccountBalance(newBalance);
            accountDao.update(account);
            logger.info("Replenished {} to account {}", amount, account);

            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDate.now());
            transaction.setTransactionAmount(amount);
            transaction.setTransactionType("replenishment");
            transaction.setAccountId(account.getAccountId());
            transaction.setRecipientAccountId(account.getAccountId());
            transactionDao.create(transaction);
            logger.info("Created transaction {}", transaction);

            generateReceipt(transaction, oldBalance, newBalance, "Replenishment");
        }
    }

    public void withdraw(AccountDto accountDto, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account account = accountDao.getById(accountDto.getAccountId());
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        synchronized (account) {
            double oldBalance = account.getAccountBalance();
            if (oldBalance < amount) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            double newBalance = oldBalance - amount;
            account.setAccountBalance(newBalance);
            accountDao.update(account);
            logger.info("Withdrawn {} from account {}", amount, account);

            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDate.now());
            transaction.setTransactionAmount(amount);
            transaction.setTransactionType("withdrawal");
            transaction.setAccountId(account.getAccountId());
            transaction.setRecipientAccountId(account.getAccountId());
            transactionDao.create(transaction);
            logger.info("Created transaction {}", transaction);

            generateReceipt(transaction, oldBalance, newBalance, "Withdrawal");
        }
    }

    public void transfer(AccountDto senderAccountDto, AccountDto recipientAccountDto, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Account senderAccount = accountDao.getById(senderAccountDto.getAccountId());
        if (senderAccount == null) {
            throw new IllegalArgumentException("Sender account not found");
        }
        Account recipientAccount = accountDao.getById(recipientAccountDto.getAccountId());
        if (recipientAccount == null) {
            throw new IllegalArgumentException("Recipient account not found");
        }
        Bank senderBank = bankDao.getById(senderAccount.getBankId());
        if (senderBank == null) {
            throw new IllegalArgumentException("Sender bank not found");
        }
        Bank recipientBank = bankDao.getById(recipientAccount.getBankId());
        if (recipientBank == null) {
            throw new IllegalArgumentException("Recipient bank not found");
        }

        Account firstLock = senderAccount.getAccountId() < recipientAccount.getAccountId() ? senderAccount : recipientAccount;
        Account secondLock = senderAccount.getAccountId() < recipientAccount.getAccountId() ? recipientAccount : senderAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                double senderOldBalance = senderAccount.getAccountBalance();
                if (senderOldBalance < amount) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                double senderNewBalance = senderOldBalance - amount;
                senderAccount.setAccountBalance(senderNewBalance);
                accountDao.update(senderAccount);
                logger.info("Transferred {} from account {} to account {}", amount, senderAccount, recipientAccount);

                double recipientOldBalance = recipientAccount.getAccountBalance();
                double recipientNewBalance = recipientOldBalance + amount;
                recipientAccount.setAccountBalance(recipientNewBalance);
                accountDao.update(recipientAccount);
                logger.info("Received {} from account {} to account {}", amount, senderAccount, recipientAccount);

                Transaction transaction = new Transaction();
                transaction.setTransactionDate(LocalDate.now());
                transaction.setTransactionAmount(amount);
                transaction.setTransactionType("transfer");
                transaction.setAccountId(senderAccount.getAccountId());
                transaction.setRecipientAccountId(recipientAccount.getAccountId());
                transactionDao.create(transaction);
                logger.info("Created transaction {}", transaction);

                generateReceipt(transaction, senderOldBalance, senderNewBalance, "Transfer Out");
                generateReceipt(transaction, recipientOldBalance, recipientNewBalance, "Transfer In");
            }
        }
    }

    private void scheduleInterestCalculation() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                calculateInterest();
            } catch (Exception e) {
                logger.error("Error calculating interest", e);
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    private void calculateInterest() {
        LocalDate today = LocalDate.now();
        int lastDayOfMonth = today.lengthOfMonth();
        if (today.getDayOfMonth() == lastDayOfMonth) {
            logger.info("Calculating interest for the end of the month");
            double interestRate = getInterestRateFromConfig();
            List<Account> accounts = accountDao.getAll();
            for (Account account : accounts) {
                synchronized (account) {
                    double oldBalance = account.getAccountBalance();
                    double interest = oldBalance * interestRate / 100;
                    double newBalance = oldBalance + interest;
                    account.setAccountBalance(newBalance);
                    accountDao.update(account);
                    logger.info("Calculated interest {} for account {}", interest, account);
                    generateReceipt(transaction, oldBalance, newBalance, "Interest");
                }
            }
        }
    }

    private double getInterestRateFromConfig() {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(CONFIG_FILE);
            Map<String, Object> config = yaml.load(inputStream);
            Object interestRate = config.get(INTEREST_RATE_KEY);
            if (interestRate instanceof Number) {
                return ((Number) interestRate).doubleValue();
            } else {
                throw new IllegalArgumentException("Invalid interest rate value in config file");
            }
        } catch (IOException e) {
            logger.error("Error reading config file", e);
            throw new RuntimeException("Error reading config file", e);
        }
    }

    private String formatDouble(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private void generateReceipt(Transaction transaction, double oldBalance, double newBalance, String operation) {
        Account account = accountDao.getById(transaction.getAccountId());
        Account recipientAccount = accountDao.getById(transaction.getRecipientAccountId());
        String fileName = String.format(RECEIPT_FORMAT, transaction.getCheckNumber(), operation);
        createDirectoryIfNotExists(RECEIPT_FOLDER);
        File file = new File(RECEIPT_FOLDER, fileName);
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
            int rowWidth = 58;
            document.add(new Paragraph(formatRow("", "", rowWidth), font));
            document.add(new Paragraph(formatRow("Bank Check", "", rowWidth), font));
            document.add(new Paragraph(formatRow("Check:", transaction.getCheckNumber(), rowWidth), font));
            document.add(new Paragraph(formatRow(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)), rowWidth), font));
            document.add(new Paragraph(formatRow("Transaction type:", operation, rowWidth), font));
            document.add(new Paragraph(formatRow("Sender's bank:", bankDao.getById(account.getBankId()).getBankName(), rowWidth), font));
            document.add(new Paragraph(formatRow("Recipient bank:", bankDao.getById(recipientAccount.getBankId()).getBankName(), rowWidth), font));
            document.add(new Paragraph(formatRow("Sender's account:", account.getAccountNumber(), rowWidth), font));
            document.add(new Paragraph(formatRow("Recipient's account:", recipientAccount.getAccountNumber(), rowWidth), font));
            document.add(new Paragraph(formatRow("Amount:", formatDouble(newBalance - oldBalance) + " BYN", rowWidth), font));
            document.add(new Paragraph(formatRow("", "", rowWidth), font));
            document.close();
            logger.info("Generated receipt for account {} and operation {}", transaction, operation);
        } catch (DocumentException | IOException e) {
            logger.error("Error generating receipt", e);
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Directory created at: " + path.toAbsolutePath());
            } catch (Exception e) {
                logger.error("Failed to create directory: " + e.getMessage());
            }
        } else {
            logger.info("Directory already exists at: " + path.toAbsolutePath());
        }
    }

    private String formatRow(String label, Object value, int width) {
        if (label.isEmpty() && value.toString().isEmpty()) {
            return "|" + "-".repeat(width - 2) + "|";
        } else if (value.toString().isEmpty()) {
            int paddingSize = (width - label.length()) / 2 - 1;
            String padding = " ".repeat(paddingSize);
            return "|" + padding + label + padding + (width % 2 == 0 ? "" : " ") + "|";
        } else {
            return "|" + String.format("%-28s", label) + String.format("%-28s", value) + "|";
        }
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountDao.getById(id);
        if (account == null) {
            return null;
        }
        return ObjectMapper.toDto(account);

    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountDao.getAll();
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(ObjectMapper.toDto(account));
        }
        return accountDtos;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        account = accountDao.create(account);
        return ObjectMapper.toDto(account);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        account = accountDao.update(account);
        return ObjectMapper.toDto(account);
    }


    @Override
    public void deleteAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        accountDao.delete(account);
    }

    @Override
    public List<AccountDto> getAllByUserId(Long userId) {
        List<Account> accounts = accountDao.getAllByUserId(userId);
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(ObjectMapper.toDto(account));
        }
        return accountDtos;
    }
}

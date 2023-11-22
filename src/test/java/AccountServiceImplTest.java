import org.example.data.dto.AccountDto;
import org.example.data.util.BankAccountNumberGenerator;
import org.example.service.AccountService;
import org.example.service.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {
    private AccountService accountService;
    @BeforeEach
    void setUp() { accountService = ServiceFactory.getInstance().getAccountService(); }

    @ParameterizedTest
    @CsvSource({"2, BY02BLBB3012000000020000", "3, BY03BLBB3012000000030000", "4, BY04BLBB3012000000040000", "5, BY05BLBB3012000000050000"})
    void getAccountById(Long id, String accountNumber) {
        AccountDto accountDto = accountService.getAccountById(id);
        assertNotNull(accountDto);
        assertEquals(id, accountDto.getAccountId());
        assertEquals(accountNumber, accountDto.getAccountNumber());
    }

    @Test
    void getAllAccounts() {
        List<AccountDto> accountDtos = accountService.getAllAccounts();
        assertNotNull(accountDtos);
        assertEquals(40, accountDtos.size());
        assertEquals("BY01BLBB3012000000010000", accountDtos.get(0).getAccountNumber());
        assertEquals("BY02BLBB3012000000020000", accountDtos.get(1).getAccountNumber());
        assertEquals("BY03BLBB3012000000030000", accountDtos.get(2).getAccountNumber());
        assertEquals("BY04BLBB3012000000040000", accountDtos.get(3).getAccountNumber());
        assertEquals("BY05BLBB3012000000050000", accountDtos.get(4).getAccountNumber());
    }

    @Test
    void createAccount() {
        AccountDto accountDto = new AccountDto();
        String accountType = "savings";
        Long bankId = 2L;
        Long userId = 1L;
        Double balance = 500.00;
        String accountNumber = BankAccountNumberGenerator.generateIBAN();
        accountDto.setAccountNumber(accountNumber);
        accountDto.setAccountBalance(balance);
        accountDto.setAccountType(accountType);
        accountDto.setUserId(userId);
        accountDto.setBankId(bankId);
        AccountDto createdAccountDto = accountService.createAccount(accountDto);
        assertNotNull(createdAccountDto);
        assertNotNull(createdAccountDto.getUserId());
        assertEquals(accountDto.getAccountNumber(), createdAccountDto.getAccountNumber());
        assertEquals(accountDto.getAccountType(), createdAccountDto.getAccountType());
        assertEquals(accountDto.getAccountBalance(), createdAccountDto.getAccountBalance());
    }

    @Test
    void updateAccount() {
        AccountDto accountDto = accountService.getAccountById(1L);
        assertNotNull(accountDto);
        accountDto.setAccountType("checking");
        AccountDto updatedAccountDto = accountService.updateAccount(accountDto);
        assertEquals(accountDto.getAccountId(), updatedAccountDto.getAccountId());
        assertEquals(accountDto.getAccountBalance(), updatedAccountDto.getAccountBalance());
        assertEquals(accountDto.getAccountNumber(), updatedAccountDto.getAccountNumber());
        assertEquals(accountDto.getBankId(), updatedAccountDto.getBankId());
        assertEquals(accountDto.getUserId(), updatedAccountDto.getUserId());
        assertEquals(accountDto.getAccountType(), updatedAccountDto.getAccountType());
    }

    @Test
    void deleteAccount() {
        AccountDto accountDto = accountService.getAccountById(1L);
        assertNotNull(accountDto);
        accountService.deleteAccount(accountDto);
        assertNull(accountService.getAccountById(1L));
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.00, 200.00, 300.00})
    void replenish(double amount) {
        AccountDto accountDto = accountService.getAccountById(2L);
        double oldBalance = accountDto.getAccountBalance();
        accountService.replenish(accountDto, amount);
        AccountDto replenishedAccountDto = accountService.getAccountById(2L);
        assertEquals(oldBalance + amount, replenishedAccountDto.getAccountBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.00, 200.00, 300.00})
    void withdraw(double amount) {
        AccountDto accountDto = accountService.getAccountById(2L);
        double oldBalance = accountDto.getAccountBalance();
        accountService.withdraw(accountDto, amount);
        AccountDto withdrawedAccountDto = accountService.getAccountById(2L);
        assertEquals(oldBalance - amount, withdrawedAccountDto.getAccountBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.00, 50.00, 100.00})
    void transfer(double amount) {
        AccountDto senderAccountDto = accountService.getAccountById(1L);
        AccountDto recipientAccountDto = accountService.getAccountById(2L);

        double oldSenderBalance = senderAccountDto.getAccountBalance();
        double oldRecipientBalance = recipientAccountDto.getAccountBalance();

        accountService.transfer(senderAccountDto, recipientAccountDto, amount);

        AccountDto tranferedSenderAccountDto = accountService.getAccountById(1L);
        AccountDto tranferedRecipientAccountDto = accountService.getAccountById(2L);

        assertEquals(oldSenderBalance - amount, tranferedSenderAccountDto.getAccountBalance());
        assertEquals(oldRecipientBalance + amount, tranferedRecipientAccountDto.getAccountBalance());
    }
}

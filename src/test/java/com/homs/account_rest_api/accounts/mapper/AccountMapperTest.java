package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.model.AccountType;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private final AccountMapper accountMapper = new AccountMapper();
    private final AccountMockFactory accountMockFactory = new AccountMockFactory();

    @Test
    void toDTO() {
        Account mainAccount = accountMockFactory.createMainAccount();
        AccountDTO dto = accountMapper.toDTO(mainAccount);

        assertEquals(mainAccount.getAccountId(), dto.getId());
        assertEquals(mainAccount.getAlias(), dto.getAlias());
        assertEquals(mainAccount.getBalance(), dto.getBalance());
        assertEquals(mainAccount.getType(), dto.getType());

        AccountDTO dto1 = accountMapper.toDTO(null);
        assertNull(dto1);
    }

    @Test
    void toCreateAccount() {
        Account mainAccount = accountMockFactory.createMainAccount();
        CreateAccount createAccount = accountMapper.toCreateAccount(mainAccount);

        assertEquals(mainAccount.getAlias(), createAccount.getAlias());
        assertEquals(mainAccount.getBalance(), createAccount.getBalance());
        assertEquals(mainAccount.getType(), createAccount.getType());

        CreateAccount createAccount1 = accountMapper.toCreateAccount(null);
        assertNull(createAccount1);
    }

    @Test
    void toEntityShouldParseAccountDTOToAccount() {
        AccountDTO dto = AccountDTO.builder()
                .id(1L)
                .alias("Main Account")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(50_000))
                .build();

        Account account = accountMapper.toEntity(dto);

        assertEquals(dto.getId(), account.getAccountId());
        assertEquals(dto.getAlias(), account.getAlias());
        assertEquals(dto.getBalance(), account.getBalance());
        assertEquals(dto.getType(), account.getType());

        Account result = accountMapper.toEntity((AccountDTO) null);
        assertNull(result);
    }

    @Test
    void toEntityShouldParseCreateAccountToAccount() {
        CreateAccount req = CreateAccount.builder()
                .balance(BigDecimal.valueOf(50_000))
                .alias("Main Account")
                .type(AccountType.SAVINGS).build();

        Account result1 = accountMapper.toEntity(req);

        assertEquals(req.getBalance(), result1.getBalance());
        assertEquals(req.getAlias(), result1.getAlias());
        assertEquals(req.getType(), result1.getType());

        Account result2 = accountMapper.toEntity((CreateAccount) null);
        assertNull(result2);
    }
}
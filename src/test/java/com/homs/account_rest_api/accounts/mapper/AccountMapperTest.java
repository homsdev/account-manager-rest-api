package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.accounts.dto.UpdateBalanceDTO;
import com.homs.account_rest_api.accounts.model.Account;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountMapperTest extends TestCase {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldMapAccountToCreateAccountDto() {
        Account account = Account.builder()
                .accountId("accountId")
                .alias("accountAlias")
                .balance(BigDecimal.valueOf(10_000))
                .build();
        CreateAccountDto dto = accountMapper.toDto(account);
        BigDecimal accountBalance = account.getBalance();
        BigDecimal dtoBalance = dto.getAccountBalance();

        assertNotNull(dto);
        assertEquals(account.getAlias(), dto.getAccountAlias());
        assertEquals(0, accountBalance.compareTo(dtoBalance));
    }

    @Test
    public void shouldMapCreateAccountDtoToAccount() {
        CreateAccountDto dto = CreateAccountDto.builder()
                .accountBalance(BigDecimal.valueOf(1_500))
                .accountAlias("Dto alias")
                .build();
        Account entity = accountMapper.toEntity(dto);
        BigDecimal accountBalance = entity.getBalance();
        BigDecimal dtoBalance = entity.getBalance();

        assertNotNull(entity);
        assertEquals(dto.getAccountAlias(), entity.getAlias());
        assertEquals(0, accountBalance.compareTo(dtoBalance));
    }

    @Test
    public void shouldMapUpdateBalanceDtoToAccount() {
        UpdateBalanceDTO dto = UpdateBalanceDTO.builder()
                .updatedBalance(BigDecimal.valueOf(2_000))
                .build();

        Account entity = accountMapper.toEntity(dto);
        BigDecimal accountBalance = entity.getBalance();
        BigDecimal dtoBalance = dto.getUpdatedBalance();
        assertNotNull(entity);
        assertEquals(0, dtoBalance.compareTo(accountBalance));
    }

    @Test
    public void shouldMapToNullWhenPassedNullValues() {
        CreateAccountDto createAccountDto = null;
        UpdateBalanceDTO updateBalanceDTO = null;
        Account account = null;
        Account entity = accountMapper.toEntity(createAccountDto);
        Account entity1 = accountMapper.toEntity(updateBalanceDTO);
        CreateAccountDto dto = accountMapper.toDto(account);

        assertNull(entity);
        assertNull(entity1);
        assertNull(dto);
    }
}
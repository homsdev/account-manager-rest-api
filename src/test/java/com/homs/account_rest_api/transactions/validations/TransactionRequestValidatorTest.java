package com.homs.account_rest_api.transactions.validations;

import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TransactionRequestValidatorTest {

    private final TransactionRequestValidator validator = new TransactionRequestValidator();

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenAmountIsNull() {
        CreateTransaction dto = CreateTransaction.builder()
                .amount(null)
                .type("INCOME")
                .date("05-05-2024")
                .alias("LAS")
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(dto));
        assertEquals(1, ex.getErrorMessages().size());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenTypeIsNullOrBlank() {
        CreateTransaction blankTypeDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("    ")
                .date("05-05-2024")
                .alias("LAS")
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(blankTypeDto));

        assertEquals(1, ex1.getErrorMessages().size());

        CreateTransaction nullTypeDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type(null)
                .date("05-05-2024")
                .alias("LAS")
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(nullTypeDto));

        assertEquals(1, ex2.getErrorMessages().size());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenTypeIsNotSupported() {
        CreateTransaction dto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("SOMETHING")
                .date("05-05-2024")
                .alias("LAS")
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(dto));
        assertEquals(1, ex.getErrorMessages().size());

        log.info("Error messages: {}", ex.getErrorMessages());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWithNullOrBlankDate() {
        CreateTransaction blankDate = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date(" ")
                .alias("test")
                .categoryId(1L)
                .accountId(1L)
                .build();
        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(blankDate));

        assertEquals(1, ex1.getErrorMessages().size());

        log.info("Error messages: {}", ex1.getErrorMessages());

        CreateTransaction nullishDate = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date(null)
                .alias("test")
                .categoryId(1L)
                .accountId(1L)
                .build();
        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(nullishDate));

        assertEquals(1, ex2.getErrorMessages().size());

        log.info("Error messages: {}", ex2.getErrorMessages());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWithBadFormatDates() {
        CreateTransaction dto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("60-05-2026")
                .alias("test")
                .categoryId(1L)
                .accountId(1L)
                .build();
        InvalidParametersException ex = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(dto));

        assertEquals(1, ex.getErrorMessages().size());

        log.info("Error messages: {}", ex.getErrorMessages());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenAliasIsNullOrBlank() {
        CreateTransaction blankAliasDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias(" ")
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(blankAliasDto));

        assertEquals(1, ex1.getErrorMessages().size());

        CreateTransaction nullAliasDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias(null)
                .categoryId(1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(nullAliasDto));

        assertEquals(1, ex2.getErrorMessages().size());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenAccountIdIsMissingOrWrong() {
        CreateTransaction nullAccountDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias("TEST")
                .categoryId(1L)
                .accountId(null)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(nullAccountDto));

        assertEquals(1, ex1.getErrorMessages().size());

        CreateTransaction negativeAccountId = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias("TEST")
                .categoryId(1L)
                .accountId(-1L)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(negativeAccountId));

        assertEquals(1, ex2.getErrorMessages().size());
    }

    @Test
    void validateCreateTransactionRequestShouldThrowExceptionWhenCategoryIdIsMissingOrWrong() {
        CreateTransaction nullCategoryIdDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias("TEST")
                .categoryId(null)
                .accountId(1L)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(nullCategoryIdDto));

        assertEquals(1, ex1.getErrorMessages().size());

        CreateTransaction incorrectCategoryIdDto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias("TEST")
                .categoryId(-1L)
                .accountId(1L)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateCreateTransactionRequest(incorrectCategoryIdDto));

        assertEquals(1, ex2.getErrorMessages().size());
    }


    @Test
    void validateCreateTransactionRequestShouldNotThrowExceptionWithCorrectRequest() {
        CreateTransaction dto = CreateTransaction.builder()
                .amount(BigDecimal.TEN)
                .type("INCOME")
                .date("05-05-2024")
                .alias("TEST")
                .categoryId(1L)
                .accountId(1L)
                .build();

        assertDoesNotThrow(() -> validator.validateCreateTransactionRequest(dto));
    }

    @Test
    void validateDateFilterTransactionRequestShouldThrowExceptionWhenAccountIdIsMissingOrWrong() {
        DateFilterTransactionReq nullAccountId = DateFilterTransactionReq.builder()
                .accountId(null)
                .year(2024)
                .month(5)
                .build();

        DateFilterTransactionReq badAccountId = DateFilterTransactionReq.builder()
                .accountId(-1L)
                .year(2024)
                .month(5)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(nullAccountId));

        assertEquals(1, ex1.getErrorMessages().size());

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(badAccountId));

        assertEquals(1, ex2.getErrorMessages().size());

    }

    @Test
    void validateDateFilterTransactionRequestShouldThrowExceptionWhenYearIsMissingOrWrong() {
        DateFilterTransactionReq missingYear = DateFilterTransactionReq.builder()
                .accountId(1L)
                .month(5)
                .build();

        InvalidParametersException ex = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(missingYear));

        assertEquals(1, ex.getErrorMessages().size());

        DateFilterTransactionReq badYear = DateFilterTransactionReq.builder()
                .accountId(1L)
                .year(-2023)
                .month(5)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(badYear));

        assertEquals(1, ex2.getErrorMessages().size());

        DateFilterTransactionReq badYear2 = DateFilterTransactionReq.builder()
                .accountId(1L)
                .year(1_000_000_000)
                .month(5)
                .build();

        InvalidParametersException ex3 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(badYear2));

        assertEquals(1, ex3.getErrorMessages().size());

    }

    @Test
    void validateDateFilterTransactionRequestShouldThrowExceptionWhenMonthIsMissingOrWrong() {
        DateFilterTransactionReq missingMonth = DateFilterTransactionReq.builder()
                .accountId(1L)
                .year(2024)
                .build();

        InvalidParametersException ex1 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(missingMonth));

        assertEquals(1, ex1.getErrorMessages().size());

        DateFilterTransactionReq badMonth = DateFilterTransactionReq.builder()
                .accountId(1L)
                .month(14)
                .year(2024)
                .build();

        InvalidParametersException ex2 = assertThrows(InvalidParametersException.class,
                () -> validator.validateDateFilterTransactionRequest(badMonth));

        assertEquals(1, ex2.getErrorMessages().size());
    }

    @Test
    void validateDateFilterTransactionRequestShouldNotThrowExceptionWithCorrectRequest() {
        DateFilterTransactionReq dto = DateFilterTransactionReq.builder()
                .accountId(1L)
                .year(2024)
                .month(5)
                .build();

        assertDoesNotThrow(() -> validator.validateDateFilterTransactionRequest(dto));
    }
}
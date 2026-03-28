package com.homs.account_rest_api.transactions.validations;

import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Component
public class TransactionRequestValidator {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public void validateCreateTransactionRequest(CreateTransaction dto) {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(dto.getAmount())) {
            errors.add("Amount cannot be null");
        }

        if (Objects.isNull(dto.getType()) || dto.getType().isBlank()) {
            errors.add("Type cannot be null or blank");
        } else if (!isValidType(dto.getType())) {
            errors.add("Type must be one of the following: INCOME, EXPENSE");
        }

        if (Objects.isNull(dto.getDate()) || dto.getDate().isBlank()) {
            errors.add("Date cannot be null or blank");
        } else if (!isValidDate(dto.getDate())) {
            errors.add("Date must match format dd-MM-yyyy");
        }

        if (Objects.isNull(dto.getAlias()) || dto.getAlias().isBlank()) {
            errors.add("Alias cannot be null");
        }

        if (Objects.isNull(dto.getAccountId()) || dto.getAccountId() < 0) {
            errors.add("Account id cannot be null");
        }

        if (Objects.isNull(dto.getCategoryId()) || dto.getCategoryId() < 0) {
            errors.add("Category id cannot be null");
        }

        if (!errors.isEmpty()) {
            throw new InvalidParametersException(errors);
        }
    }

    public void validateDateFilterTransactionRequest(DateFilterTransactionReq dto) {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(dto.getAccountId()) || dto.getAccountId() < 0) {
            errors.add("Account id cannot be null");
        }

        if (Objects.isNull(dto.getYear())) {
            log.error("Year cannot be null");
            errors.add("Year cannot be null");
        } else if (!isValidYear(dto.getYear())) {
            log.error("Year must be a valid year");
            errors.add("Year must be a valid year");
        }

        if (Objects.isNull(dto.getMonth())) {
            errors.add("Month cannot be null");
        } else if (!isValidMonth(dto.getMonth())) {
            errors.add("Month must be a valid value between 1 and 12");
        }

        if (!errors.isEmpty()) {
            throw new InvalidParametersException(errors);
        }
    }

    private boolean isValidYear(Integer year) {

        if (year <= 0) {
            return false;
        }

        try {
            Year.of(year);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean isValidMonth(Integer month) {
        try {
            Month.of(month);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private boolean isValidType(String type) {
        try {
            TransactionType.valueOf(type.toUpperCase(Locale.ROOT));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}

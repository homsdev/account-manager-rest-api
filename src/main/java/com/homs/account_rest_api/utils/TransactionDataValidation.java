package com.homs.account_rest_api.utils;

import com.homs.account_rest_api.transactions.model.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TransactionDataValidation {

    private TransactionDataValidation(){

    }

    /**
     * Validates the given {@link Transaction} object by checking if all required fields are non-null.
     * The fields that are checked for null values are:
     * <ul>
     *     <li>transactionId</li>
     *     <li>type</li>
     *     <li>date</li>
     *     <li>account</li>
     *     <li>alias</li>
     * </ul>
     *
     * <p>If any of the required fields are null, the method returns {@code false}, indicating that the transaction is invalid.
     * If all the required fields are non-null, the method returns {@code true}, indicating that the transaction is valid.</p>
     *
     * @param transaction The {@link Transaction} object to be validated. This should not be {@code null}.
     * @return {@code true} if all required fields of the transaction are non-null, otherwise {@code false}.
     *
     * @see Transaction
     */
    public static Boolean isValid(final Transaction transaction) {
        if (transaction == null) return false;

        List<Object> fieldsToCheck = Arrays.asList(
                transaction.getTransactionId(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getAccount(),
                transaction.getAlias()
        );

        return fieldsToCheck.stream().noneMatch(Objects::isNull);
    }
}

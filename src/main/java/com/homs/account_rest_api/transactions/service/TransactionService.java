package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import com.homs.account_rest_api.transactions.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto createTransaction(CreateTransaction dto);
    List<TransactionDto> findTransactionsByAccountAndMonth(DateFilterTransactionReq dto);
}

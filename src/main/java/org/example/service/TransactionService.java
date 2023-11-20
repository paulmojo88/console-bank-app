package org.example.service;

import org.example.data.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto getTransactionById(Long id);
    List<TransactionDto> getAllTransactions();
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction(TransactionDto transactionDto);
    void deleteTransaction(TransactionDto transactionDto);
}

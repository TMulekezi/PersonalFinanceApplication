package com.gfinance.application.dao;

import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;

import java.util.List;

public interface TransactionDao {
    List<Transaction> retrieveTransactionsByUserName(String userName);

    Transaction findTransactionById(long id);

    void deleteTransactionById(int theId);

    void deleteTransaction(Transaction t);

    void saveTransaction(Transaction t);
}

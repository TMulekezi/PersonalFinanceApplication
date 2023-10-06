package com.gfinance.application.dao;

import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao{
    private EntityManager entityManager;

    public TransactionDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<Transaction> retrieveTransactionsByUserName(String userName) {
        TypedQuery<Transaction> query = entityManager.createQuery("select t from Transaction t where t.user.username=:name", Transaction.class);

        query.setParameter("name", userName);

        List<Transaction> transactions = query.getResultList();


        return transactions;
    }

    @Override
    public Transaction findTransactionById(long id) {
        return entityManager.find(Transaction.class, id);
    }


    @Override
    @Transactional
    public void deleteTransactionById(int theId) {
        Transaction t = entityManager.find(Transaction.class, theId);
        entityManager.remove(t);
        Transaction t2 = entityManager.find(Transaction.class, theId);
    }

    @Override
    public void deleteTransaction(Transaction t) {
        entityManager.remove(t);
    }

    @Override
    @Transactional
    public void saveTransaction(Transaction t) {
        entityManager.persist(t);
    }
}

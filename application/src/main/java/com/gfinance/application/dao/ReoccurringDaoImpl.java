package com.gfinance.application.dao;

import com.gfinance.application.entity.Reoccurring;
import com.gfinance.application.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReoccurringDaoImpl implements ReoccurringDao{

    private EntityManager entityManager;

    @Autowired
    public ReoccurringDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void deleteReoccurringPaymentById(long id) {
        Reoccurring payment = entityManager.find(Reoccurring.class, id);

        entityManager.remove(payment);
    }

    @Override
    @Transactional
    public void saveReoccurringPayment(Reoccurring r) {
        entityManager.persist(r);
    }

    @Override
    @Transactional
    public void updateReoccurringPayment(Reoccurring r) {
        entityManager.merge(r);
    }

    @Override
    public Reoccurring findReoccurringById(long id) {
        return entityManager.find(Reoccurring.class, id);
    }


}

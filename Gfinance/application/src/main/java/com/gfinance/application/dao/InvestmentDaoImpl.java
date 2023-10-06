package com.gfinance.application.dao;


import com.gfinance.application.entity.Investment;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class InvestmentDaoImpl implements InvestmentDao{

    private EntityManager entityManager;

    public InvestmentDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void deleteInvestment(Investment investment) {
        entityManager.remove(investment);
    }

    @Override
    public Investment findInvestmentById(long id) {
        Investment i = entityManager.find(Investment.class, id);
        return i;
    }

    @Override
    @Transactional
    public void saveInvestment(Investment i) {
        entityManager.persist(i);
    }
}

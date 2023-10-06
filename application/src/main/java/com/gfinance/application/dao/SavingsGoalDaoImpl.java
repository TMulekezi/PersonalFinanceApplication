package com.gfinance.application.dao;

import com.gfinance.application.entity.SavingsGoal;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class SavingsGoalDaoImpl implements SavingsGoalDao{

    private EntityManager entityManager;

    public SavingsGoalDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public void save(SavingsGoal goal) {
        entityManager.persist(goal);
    }

    @Override
    @Transactional
    public void deleteSavingsGoalById(long id) {
        SavingsGoal goal = entityManager.find(SavingsGoal.class, id);
        entityManager.remove(goal);
    }

    @Override
    public SavingsGoal findGoalById(long id) {
        return entityManager.find(SavingsGoal.class, id);
    }
}

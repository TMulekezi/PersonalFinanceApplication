package com.gfinance.application.dao;

import com.gfinance.application.entity.Checking;
import com.gfinance.application.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CheckingDaoImpl implements CheckingDao{

    EntityManager entityManager;

    @Autowired
    public CheckingDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public boolean updateCheckingAmountById(long id, double amount) {
        Checking checkingAccount = entityManager.find(Checking.class, id);
        User u = checkingAccount.getUser();

        double currentAmount = checkingAccount.getAmount();

        if (checkingAccount.getAmount() - amount < 0)  {
            return false;
        }

        u.getChecking().setAmount(currentAmount + amount);

        entityManager.merge(u);
        return true;
    }

    @Override
    public Checking findCheckingByUserId(long id) {
        TypedQuery<Checking> query = entityManager.createQuery("from Checking c where c.user.id=:uid", Checking.class);
        query.setParameter("uid", id);
        Checking account = query.getSingleResult();
        return account;
    }
}

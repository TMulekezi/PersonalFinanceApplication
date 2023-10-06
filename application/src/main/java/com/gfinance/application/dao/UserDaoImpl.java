package com.gfinance.application.dao;

import com.gfinance.application.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }
    @Override
    public User findByUsername(String theName) {

        TypedQuery<User> theQuery = entityManager.createQuery("from User where username=:uName", User.class);

        theQuery.setParameter("uName", theName);

        User tempUser;
        try {

            tempUser = theQuery.getSingleResult();
        } catch (Exception e) {
            tempUser = null;
        }
        return tempUser;
    }
    @Override
    @Transactional
    public void save(User theUser) {
        entityManager.persist(theUser);
    }
    @Override
    public List<Transaction> findUserTransactionsById(long id) {
        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid", Transaction.class);
        query.setParameter("uid", id);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    public List<Transaction> findEssentialUserTransactionsById(long id) {
        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.essential=1", Transaction.class);
        query.setParameter("uid", id);
        List<Transaction> transactions = query.getResultList();

        return transactions;
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndDay(long id, LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startOfDay = date.toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfDay);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfDay);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);

        query.setParameter("end", e);

        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndWeek(long id, LocalDateTime date) {
        date = date.toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime startOfWeek = date.minusDays(date.getDayOfWeek().getValue()-1);
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        endOfWeek = endOfWeek.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfWeek);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfWeek);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);
        query.setParameter("end", e);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndMonth(long id, LocalDateTime date) {
        date = date.toLocalDate().atTime(LocalTime.MIN);
        boolean isLeapYear = date.toLocalDate().isLeapYear();

        LocalDateTime startOfMonth = date.withDayOfMonth(1);

        LocalDateTime endOfMonth = date.withDayOfMonth(date.getMonth().length(isLeapYear));
        endOfMonth = endOfMonth.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfMonth);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfMonth);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);
        query.setParameter("end", e);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    @Transactional
    public boolean updateUserCheckingAccount(User u, double amount) {
        Checking checkingAccount = u.getChecking();


        double currentAmount = checkingAccount.getAmount();

        if (checkingAccount.getAmount() + amount < 0)  {
            return false;
        }

        checkingAccount.setAmount(currentAmount + amount);
        entityManager.merge(u);
        return true;
    }

    @Override
    @Transactional
    public boolean updateUserSavingsAccount(User u, double amount) {
        Savings savingsAccount = u.getSavings();
        double currentAmount = savingsAccount.getAmount();

        if (savingsAccount.getAmount() + amount < 0)  {
            return false;
        }

        savingsAccount.setAmount(currentAmount + amount);
        entityManager.merge(u);
        return true;
    }

    @Override
    @Transactional
    public void updateUser(User u) {
        entityManager.merge(u);
    }

    @Override
    public List<SavingsGoal> findUserSavingsGoalsById(long id) {
        TypedQuery<SavingsGoal> query = entityManager.createQuery("select g From User u JOIN u.savingsGoals g where u.id=:uid", SavingsGoal.class);
        query.setParameter("uid", id);
        List<SavingsGoal> goals = query.getResultList();
        return goals;
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndDay(long id, LocalDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startOfDay = date.toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfDay);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfDay);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end AND t.recurring=:num ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);
        query.setParameter("end", e);
        query.setParameter("num", 0);
        List<Transaction> transactions = query.getResultList();

        return transactions;
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndWeek(long id, LocalDateTime date) {
        date = date.toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime startOfWeek = date.minusDays(date.getDayOfWeek().getValue()-1);
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        endOfWeek = endOfWeek.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfWeek);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfWeek);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end AND t.recurring=:num ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);
        query.setParameter("end", e);
        query.setParameter("num", 0);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndMonth(long id, LocalDateTime date) {
        date = date.toLocalDate().atTime(LocalTime.MIN);
        boolean isLeapYear = date.toLocalDate().isLeapYear();

        LocalDateTime startOfMonth = date.withDayOfMonth(1);

        LocalDateTime endOfMonth = date.withDayOfMonth(date.getMonth().length(isLeapYear));
        endOfMonth = endOfMonth.toLocalDate().atTime(LocalTime.MAX);
        java.sql.Timestamp s = java.sql.Timestamp.valueOf(startOfMonth);
        java.sql.Timestamp e = java.sql.Timestamp.valueOf(endOfMonth);

        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM User u JOIN u.transactions t WHERE u.id=:uid AND t.date BETWEEN :start AND :end AND t.recurring=:num ORDER BY t.date DESC", Transaction.class);

        query.setParameter("uid",id);

        query.setParameter("start", s);
        query.setParameter("end", e);
        query.setParameter("num", 0);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    @Override
    public List<Investment> findUserInvestmentsById(long id) {

        TypedQuery<Investment> query = entityManager.createQuery("Select i From User u Join u.investments i where u.id=:uid", Investment.class);
        query.setParameter("uid", id);

        List<Investment> investments = query.getResultList();

        return investments;
    }

    @Override
    public List<Achievement> findUserBudgetAchievementById(long id) {
        TypedQuery<Achievement> query = entityManager.createQuery("Select a FROM User u JOIN u.achievements a WHERE u.id=:uid AND a.type=:atype", Achievement.class);
        query.setParameter("uid", id);
        query.setParameter("atype", "budget");
        List<Achievement> achievements = query.getResultList();
        return achievements;
    }

    @Override
    public List<Achievement> findUserSavingsAchievementById(long id) {
        TypedQuery<Achievement> query = entityManager.createQuery("Select a FROM User u JOIN u.achievements a WHERE u.id=:uid AND a.type=:atype", Achievement.class);
        query.setParameter("uid", id);
        query.setParameter("atype", "savings");
        List<Achievement> achievements = query.getResultList();
        return achievements;
    }

    @Override
    public List<Reoccurring> findUserReoccurringPaymentsById(long id) {
        TypedQuery<Reoccurring> query = entityManager.createQuery("SELECT r FROM User u JOIN u.reoccurringPayments r WHERE u.id=:id", Reoccurring.class);
        query.setParameter("id", id);
        List<Reoccurring> rp = query.getResultList();
        return rp;
    }

    @Override
    @Transactional
    public void deleteUser(User u) {
        entityManager.remove(u);
    }


    //private static int dayToInt(String)
}



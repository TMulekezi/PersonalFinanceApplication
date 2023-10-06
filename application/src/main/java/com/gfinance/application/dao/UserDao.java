package com.gfinance.application.dao;

import com.gfinance.application.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDao {
    User findByUsername(String theName);
    void save(User theUser);

    List<Transaction> findUserTransactionsById(long id);

    List<Transaction> findEssentialUserTransactionsById(long id);

    List<Transaction> findTransactionByUserIdAndDay(long id, LocalDateTime date);

    List<Transaction> findTransactionByUserIdAndWeek(long id, LocalDateTime date);

    List<Transaction> findTransactionByUserIdAndMonth(long id, LocalDateTime date);

    boolean updateUserCheckingAccount(User u, double amount);

    boolean updateUserSavingsAccount(User u, double amount);

    void updateUser(User u);


    List<SavingsGoal> findUserSavingsGoalsById(long id);

    List<Transaction> findOneTimeTransactionsByUserIdAndDay(long id, LocalDateTime date);

    List<Transaction> findOneTimeTransactionsByUserIdAndWeek(long id, LocalDateTime date);

    List<Transaction> findOneTimeTransactionsByUserIdAndMonth(long id, LocalDateTime date);

    List<Investment> findUserInvestmentsById(long id);

    List<Achievement> findUserBudgetAchievementById(long id);

    List<Achievement> findUserSavingsAchievementById(long id);

    List<Reoccurring> findUserReoccurringPaymentsById(long id);

    void deleteUser(User u);
}

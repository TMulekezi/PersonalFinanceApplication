package com.gfinance.application.service;

import com.gfinance.application.dao.SavingsGoalDao;
import com.gfinance.application.entity.*;
import com.gfinance.application.user.WebUser;

import java.time.LocalDateTime;
import java.util.List;
// UserService  interface facade that incorporates DAO methods to be used by controllers.
public interface UserService {

    void deleteUser (User u);
    User findByUserName(String userName);

    void save(WebUser webUser);

    List<Transaction> findUserTransactions(String userName);

    List<Transaction> findUserTransactions(long userId);

    List<Transaction> findEssentialUserTransactions(long userId);

    List<Transaction> findTransactionByUserIdAndDay(long id, LocalDateTime date);

    List<Transaction> findTransactionByUserIdAndWeek(long id, LocalDateTime date);

    List<Transaction> findTransactionByUserIdAndMonth(long id, LocalDateTime date);

    void deleteTransactionById(int theId);

    void deleteTransaction(Transaction t);

    boolean updateCheckingAmountById(long id, double amount);
    boolean updateUserCheckingAccount(User u, double amount);

    boolean updateUserSavingsAccount(User u, double amount);

    Checking findCheckingByUserId(long id);

    void saveTransaction(Transaction t);

    Transaction findTransactionById(long id);

    List<SavingsGoal> findUserSavingsGoalById(long id);

    void saveSavingsGoal(SavingsGoal goal);

    void deleteSavingsGoalById(long id);

    SavingsGoal findSavingsGoalById(long id);

    void updateUser(User u);

    List<Transaction> findOneTimeTransactionsByUserIdAndDay(long id, LocalDateTime date);

    List<Transaction> findOneTimeTransactionsByUserIdAndWeek(long id, LocalDateTime date);

    List<Transaction> findOneTimeTransactionsByUserIdAndMonth(long id, LocalDateTime date);

    void deleteInvestment(Investment investment);

    List<Investment> findInvestmentsByUserId(long id);

    Investment findInvestmentById (long id);

    void saveInvestment(Investment i);

    List<Achievement> findUserBudgetAchievementsById(long id);

    List<Achievement> findUserSavingsAchievementsById(long id);

    List<Reoccurring> findReoccurringPaymentsById(long id);

    void deleteReoccurringById(long id);

    void saveReoccurring(Reoccurring r);

    void updateReoccurring(Reoccurring r);

    Reoccurring findReoccurringById(long id);
}

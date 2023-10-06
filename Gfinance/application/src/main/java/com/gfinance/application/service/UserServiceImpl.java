package com.gfinance.application.service;

import com.gfinance.application.dao.*;
import com.gfinance.application.entity.*;
import com.gfinance.application.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
// UserService interface facade implementation that incorporates DAO methods to be used by controllers.
@Service
public class UserServiceImpl implements UserService{

    private UserDao userDao;

    private RoleDao roleDao;

    private TransactionDao transactionDao;

    private CheckingDao checkingDao;

    private SavingsGoalDao savingsGoalDao;

    private InvestmentDao investmentDao;

    private ReoccurringDao reoccurringDao;

    private AchievementDao achievementDao;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, TransactionDao transactionDao, CheckingDao checkingDao,
                           SavingsGoalDao savingsGoalDao, InvestmentDao investmentDao, ReoccurringDao reoccurringDao,
                           AchievementDao achievementDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.transactionDao = transactionDao;
        this.checkingDao = checkingDao;
        this.savingsGoalDao = savingsGoalDao;
        this.passwordEncoder = passwordEncoder;
        this.investmentDao = investmentDao;
        this.reoccurringDao = reoccurringDao;
        this.achievementDao = achievementDao;
    }

    @Override
    public void deleteUser(User u) {
        userDao.deleteUser(u);
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUsername(userName);
    }


    @Override
    public void save(WebUser webUser) {
        User user = new User();
        user.setUsername(webUser.getUserName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setEnabled(1);

        Role role = roleDao.findRoleByName("ROLE_USER");
        user.addRole(role);


        user.setChecking(new Checking(user));
        user.setSavingsStreak(new SavingsStreak());
        user.setSavings(new Savings(user));
        user.setSavingsStreak(new SavingsStreak(user));
        user.setBudgetStreak(new BudgetStreak(user));
        List<Achievement> achievements = achievementDao.findAchievements();
        user.setAchievements(achievements);
        userDao.save(user);

    }

    @Override
    public List<Transaction> findUserTransactions(String userName) {
        return transactionDao.retrieveTransactionsByUserName(userName);
    }

    @Override
    public List<Transaction> findUserTransactions(long userId) {
        return userDao.findUserTransactionsById(userId);
    }

    @Override
    public List<Transaction> findEssentialUserTransactions(long userId) {
        return userDao.findEssentialUserTransactionsById(userId
        );
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndDay(long id, LocalDateTime date) {
        return userDao.findTransactionByUserIdAndDay(id, date);
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndWeek(long id, LocalDateTime date) {
        return userDao.findTransactionByUserIdAndWeek(id, date);
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndMonth(long id, LocalDateTime date) {

        return userDao.findTransactionByUserIdAndMonth(id, date);
    }

    @Override
    public void deleteTransactionById(int theId) {
        transactionDao.deleteTransactionById(theId);
    }

    @Override
    public void deleteTransaction(Transaction t) {
        transactionDao.deleteTransaction(t);
    }

    @Override
    public boolean updateCheckingAmountById(long id, double amount) {
        return checkingDao.updateCheckingAmountById(id, amount);
    }

    @Override
    public boolean updateUserCheckingAccount(User u, double amount) {
        return userDao.updateUserCheckingAccount(u, amount);
    }

    @Override
    public boolean updateUserSavingsAccount(User u, double amount) {
        return userDao.updateUserSavingsAccount(u, amount);
    }

    @Override
    public Checking findCheckingByUserId(long id) {
        return checkingDao.findCheckingByUserId(id);
    }

    @Override
    public void saveTransaction(Transaction t) {
        transactionDao.saveTransaction(t);
    }

    @Override
    public Transaction findTransactionById(long id) {
        return transactionDao.findTransactionById(id);
    }

    @Override
    public List<SavingsGoal> findUserSavingsGoalById(long id) {
        return userDao.findUserSavingsGoalsById(id);
    }

    @Override
    public void saveSavingsGoal(SavingsGoal goal) {
        savingsGoalDao.save(goal);
    }

    @Override
    public void deleteSavingsGoalById(long id) {
        savingsGoalDao.deleteSavingsGoalById(id);
    }

    @Override
    public SavingsGoal findSavingsGoalById(long id) {
        return savingsGoalDao.findGoalById(id);
    }

    @Override
    public void updateUser(User u) {
        userDao.updateUser(u);
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndDay(long id, LocalDateTime date) {
        return userDao.findOneTimeTransactionsByUserIdAndDay(id, date);
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndWeek(long id, LocalDateTime date) {
        return userDao.findOneTimeTransactionsByUserIdAndWeek(id, date);
    }

    @Override
    public List<Transaction> findOneTimeTransactionsByUserIdAndMonth(long id, LocalDateTime date) {
        return userDao.findOneTimeTransactionsByUserIdAndMonth(id, date);
    }

    @Override
    public void deleteInvestment(Investment investment) {
        investmentDao.deleteInvestment(investment);
    }

    @Override
    public List<Investment> findInvestmentsByUserId(long id) {
        return userDao.findUserInvestmentsById(id);
    }

    @Override
    public Investment findInvestmentById(long id) {
        return investmentDao.findInvestmentById(id);
    }

    @Override
    public void saveInvestment(Investment i) {
        investmentDao.saveInvestment(i);
    }

    @Override
    public List<Achievement> findUserBudgetAchievementsById(long id) {
        return userDao.findUserBudgetAchievementById(id);
    }

    @Override
    public List<Achievement> findUserSavingsAchievementsById(long id) {
        return userDao.findUserSavingsAchievementById(id);
    }

    @Override
    public List<Reoccurring> findReoccurringPaymentsById(long id) {
        return userDao.findUserReoccurringPaymentsById(id);
    }

    @Override
    public void deleteReoccurringById(long id) {
        reoccurringDao.deleteReoccurringPaymentById(id);
    }

    @Override
    public void saveReoccurring(Reoccurring r) {
        reoccurringDao.saveReoccurringPayment(r);
    }

    @Override
    public void updateReoccurring(Reoccurring r) {
        reoccurringDao.updateReoccurringPayment(r);
    }

    @Override
    public Reoccurring findReoccurringById(long id) {
        return reoccurringDao.findReoccurringById(id);
    }


}

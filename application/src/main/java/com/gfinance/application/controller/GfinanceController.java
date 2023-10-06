package com.gfinance.application.controller;

import com.gfinance.application.entity.Reoccurring;
import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
//controller that handles the main page logic and endpoint management.
@Controller
@RequestMapping("/gfinance")
public class GfinanceController {

    private UserService userService;

    private long userId;


    @Autowired
    public GfinanceController(UserService userService) {
        this.userService = userService;
    }


    // method that returns error page incase of application exception occurence
    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }


    // method that pre processes  and returns main page display
    @GetMapping("/main")
    public String showMain(Principal principal, Model theModel) {
        // retrieve user info
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();

        //  retrieve and process any unchecked reoccurring payments
        List<Reoccurring> reoccurringPayments = userService.findReoccurringPaymentsById(userId);

        if(reoccurringPayments == null) {
            reoccurringPayments = new ArrayList<>();
        }

        // iterate through list of reoccurring payments and process any outstanding payments
        for (Reoccurring p: reoccurringPayments) {
            LocalDateTime newRenewDate = p.getRenewDate().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
            LocalDateTime newLastPaymentDate = p.getDateLastRenewed().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);

            // if renewal is due create transaction
            while (newRenewDate.isBefore(LocalDateTime.now())) {

                Transaction newTransaction = new Transaction(p.getName(),1, p.getExpense(), p.getEssential(), p.getType(), Timestamp.valueOf(newRenewDate), p.getAmount(), user);


                if(newTransaction.getExpense()==1) {
                    if(!userService.updateUserCheckingAccount(user, -newTransaction.getAmount())) {
                        // if insufficient funds, flag reoccurring payment
                        p.setLastPaymentFailed(true);
                    } else {
                        // save successful transaction
                        userService.saveTransaction(newTransaction);
                        p.setLastPaymentFailed(false);
                    }
                } else {
                    // process and save successful transaction
                    userService.updateUserCheckingAccount(user, newTransaction.getAmount());
                    userService.saveTransaction(newTransaction);
                }

                // update reoccurring payment to next renewal date
                newRenewDate = newRenewDate.plusMonths(1);
                newLastPaymentDate = newLastPaymentDate.plusMonths(1);

            }
            // update reoccurring payment once updated fully
            p.setRenewDate(Timestamp.valueOf(newRenewDate));
            p.setDateLastRenewed(Timestamp.valueOf(newLastPaymentDate));
//            userService.saveReoccurring(p);

            userService.updateReoccurring(p);
        }

        // append user streak and account information to view model.
        theModel.addAttribute("account", user.getChecking());
        theModel.addAttribute("savings", user.getSavings());
        theModel.addAttribute("budgetStreak", user.getBudgetStreak());
        theModel.addAttribute("savingsStreak", user.getSavingsStreak());
        return "main-app";
    }

    // method that handles user streak info submission
    @GetMapping("/processStreakSubmit")
    public String processStreakSubmit(Principal principal, Model theModel) {
        // retrieve user information
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();
        // retrieve streak information
        double dailySavingsTarget = user.getSavings().getSavings_target();
        double dailySavingsContribution = user.getSavings().getSavingsContribution();
        double dailyBudgetTarget = user.getChecking().getBudget();
        // find transactions of the current date and calculate sum
        List<Transaction> currentDayTransactions = userService.findOneTimeTransactionsByUserIdAndDay(userId, LocalDateTime.now());
        double dailyTransactionSum = transactionsSum(currentDayTransactions);
        // break down streak date information
        LocalDateTime lastDateCheckedSavings = user.getSavingsStreak().getDateLastConfirmed().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime lastDateCheckedBudget = user.getBudgetStreak().getDateLastConfirmed().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime previousDay = startOfDay.minusDays(1);
        // display streak information if already checked as well as "already submitted" message
        if (lastDateCheckedSavings.equals(startOfDay)) {
            theModel.addAttribute("error", "submitted");
            theModel.addAttribute("account", user.getChecking());
            theModel.addAttribute("savings", user.getSavings());
            theModel.addAttribute("budgetStreak", user.getBudgetStreak());
            theModel.addAttribute("savingsStreak", user.getSavingsStreak());
            return "main-app";
        } else if (lastDateCheckedSavings.equals(previousDay)) {
            // update streak information if current day spending habits not submitted and the last date checked was prior day
            if (dailySavingsContribution >= dailySavingsTarget) {
                // increment savings streak if daily target met
                user.getSavingsStreak().setCurrentStreak(user.getSavingsStreak().getCurrentStreak() + 1);
            } else {
                // reset savings streak if daily target not met
                user.getSavingsStreak().setCurrentStreak(0);
            }
            // set date last checked to current date
            user.getSavingsStreak().setDateLastConfirmed(java.sql.Timestamp.valueOf(startOfDay));
        } else {
            // set savings streak info to 0 if previous day was not confirmed.
            user.getSavingsStreak().setCurrentStreak(0);
            // set date last checked to current date
            user.getSavingsStreak().setDateLastConfirmed(java.sql.Timestamp.valueOf(startOfDay));
        }

        // reset the savings contribution back to zero for net day
        user.getSavings().setSavingsContribution(0);

        // update budget streak information using the same logic as savings streak process, based on retrieved information
        if (lastDateCheckedBudget.equals(startOfDay)) {
            // handled in savings streak part
        } else if (lastDateCheckedBudget.equals(previousDay)) {

            if(dailyBudgetTarget - dailyTransactionSum >= 0) {
                user.getBudgetStreak().setCurrentStreak(user.getBudgetStreak().getCurrentStreak() + 1);
            } else {
                user.getBudgetStreak().setCurrentStreak(0);
            }
            user.getBudgetStreak().setDateLastConfirmed(java.sql.Timestamp.valueOf(startOfDay));
        } else {
            user.getBudgetStreak().setCurrentStreak(0);
            user.getBudgetStreak().setDateLastConfirmed(java.sql.Timestamp.valueOf(startOfDay));
        }

        if (user.getBudgetStreak().getCurrentStreak() > user.getBudgetStreak().getRecordStreak()) {
            user.getBudgetStreak().setRecordStreak(user.getBudgetStreak().getCurrentStreak());
        }

        if (user.getSavingsStreak().getCurrentStreak() > user.getSavingsStreak().getRecordStreak()) {
            user.getSavingsStreak().setRecordStreak(user.getSavingsStreak().getCurrentStreak());
        }
        // update user information
        userService.updateUser(user);
        return "redirect:/gfinance/main";
    }

    // helper method that returns the sum of transaction amounts from a provided list.
    private static double transactionsSum(List<Transaction> list) {


        double totalSum = 0;

        if (list == null) {
            list = new ArrayList<>();
        }

        for (Transaction t: list) {
            if (t.getExpense() == 1) {
                totalSum += t.getAmount();
            }
        }

        return totalSum;
    }

}

package com.gfinance.application.controller;

import ch.qos.logback.core.model.conditional.ThenModel;
import com.gfinance.application.entity.*;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebDate;
import com.gfinance.application.user.WebSavingsGoal;
import com.gfinance.application.user.WebSavingsTransaction;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// controller that handles the logic and endpoints of the Savings page.
@Controller
@RequestMapping("/gfinance")
public class SavingsController {
    private UserService userService;

    private long userId;

    private List<SavingsGoal> goals;

    private List<WebSavingsGoal> webGoals;

    public SavingsController(UserService userService) {
        this.userService = userService;
        webGoals=new ArrayList<>();

    }

    // method that retrieves, preprocesses user savings account information and savings goals.
    @GetMapping("/savings")
    public String savings(Principal principal, Model theModel) {
        // retrieve userinformation
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

        // process savings goals and display to the returned html view page

        goals = userService.findUserSavingsGoalById(userId);

        if ( goals==null) {
            goals=new ArrayList<>();
        }

        webGoals.clear();

        for (SavingsGoal goal: goals) {

            webGoals.add(new WebSavingsGoal(goal, user.getSavings(), goal.getId()));
        }



        theModel.addAttribute("goals", webGoals);
        theModel.addAttribute("savingsAccount", user.getSavings());

        return "savings";
    }


    // method that returns savings goal form
    @GetMapping("/savingsGoalForm")
    public String showSavingsGoalForm(Model theModel, Principal principal) {
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();

        theModel.addAttribute("savingsAccount", user.getSavings());
        theModel.addAttribute("goal", new SavingsGoal());
        return "savings-goal-form";
    }



    // method that processes a new savings goal
    @PostMapping("/processSavingsGoalForm")
    public String processSavingsGoalForm(@Valid @ModelAttribute("goal") WebSavingsGoal goal, BindingResult theBindingResult,
                                         Model theModel, Principal principal) {

        // if form has errors return form
        if (theBindingResult.hasErrors()) {
            return "savings-goal-form";
        }
        User user = userService.findByUserName(principal.getName());
        SavingsGoal newGoal = new SavingsGoal(goal.getAmount(), goal.getName());
        newGoal.setUser(user);
        userService.saveSavingsGoal(newGoal);
        return "redirect:/gfinance/savings";
    }

    // method that process the deletion of a savings goal given a goalId
    @GetMapping("/processGoalDelete")
    public String processGoalDelete(@RequestParam("goalId") int goalId, Principal principal) {
        // retrieve user information
        User user = userService.findByUserName(principal.getName());

        // return savings goal
        SavingsGoal g = userService.findSavingsGoalById(goalId);


        if (g != null) {
            // if savings goal exists check if user has permission to delete
            if (g.getId() != user.getId()) {
                return "redirect:/gfinance/savings";
            }
            // delete savings goal
            userService.deleteSavingsGoalById(goalId);
        }


        return "redirect:/gfinance/savings";
    }

    // method that displays the form for updating user savings target/goal
    @GetMapping("/savingsGoalUpdateForm")
    public String showSavingsGoalUpdateForm(Principal principal, Model theModel) {
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();
        goals = userService.findUserSavingsGoalById(userId);

        theModel.addAttribute("goals", goals);
        theModel.addAttribute("savingsAccount", user.getSavings());
        return "savings-goal-update-form";
    }

    // method that processes new savings target submitted by user.
    @PostMapping("/processSavingsGoalUpdateForm")
    public String processSavingsGoalUpdateForm(@Valid @ModelAttribute("savingsAccount") Savings savingsAccount, BindingResult theBindingResult,
                                         Model theModel, Principal principal) {
        User user = userService.findByUserName(principal.getName());
        user.getSavings().setSavings_target(savingsAccount.getSavings_target());
        userService.updateUser(user);
        return "redirect:/gfinance/savings";
    }

    // method that returns form for transactions between savings and budget accounts
    @GetMapping("/savingsTransactionForm")
    public String showSavingsTransactionForm(Model theModel, Principal principal) {
        User user = userService.findByUserName(principal.getName());
        theModel.addAttribute("savingsAccount", user.getSavings());
        theModel.addAttribute("savingsTransaction", new WebSavingsTransaction());
        return "savings-transaction-form";
    }

    // method that processes a new savings transaction
    @PostMapping("/processSavingsTransactionForm")
    public String processSavingsGoalUpdateForm(@Valid @ModelAttribute("savingsTransaction") WebSavingsTransaction savingsTransaction, BindingResult theBindingResult,
                                               Model theModel, Principal principal) {

        // retrieve user information
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();
        goals = userService.findUserSavingsGoalById(userId);

        theModel.addAttribute("goals", goals);
        theModel.addAttribute("savingsAccount", user.getSavings());

        // return form if submitted page has errors
        if (theBindingResult.hasErrors()) {

            return "savings-transaction-form";
        }

        // process transaction between accounts
        if (savingsTransaction.getType().equals("add")) {
            if(!userService.updateUserCheckingAccount(user, -savingsTransaction.getAmount())) {
                theModel.addAttribute("checkingError", "insufficient checking funds");
                return "savings-transaction-form";
            }
            userService.updateUserSavingsAccount(user, savingsTransaction.getAmount());
            user.getSavings().setSavingsContribution(user.getSavings().getSavingsContribution() + savingsTransaction.getAmount());
            Transaction newTransaction = new Transaction("account transfer",0,1,1,"transfer", Timestamp.valueOf(LocalDateTime.now()), savingsTransaction.getAmount(), user);
            userService.saveTransaction(newTransaction);
            userService.updateUser(user);
        } else {
            if(!userService.updateUserSavingsAccount(user, -savingsTransaction.getAmount())) {
                theModel.addAttribute("savingsError", "insufficient savings funds");
                return "savings-transaction-form";
            }
            userService.updateUserCheckingAccount(user, savingsTransaction.getAmount());
            Transaction newTransaction = new Transaction("account transfer",0,0,1,"transfer", Timestamp.valueOf(LocalDateTime.now()), savingsTransaction.getAmount(), user);
            userService.saveTransaction(newTransaction);
        }

        return "redirect:/gfinance/savings";
    }


}

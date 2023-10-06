package com.gfinance.application.controller;

import com.gfinance.application.entity.Reoccurring;
import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebReoccurring;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// controller that handles reoccurring page logic and endpoints
@Controller
@RequestMapping("/gfinance")
public class ReoccurringController {

    private User user;

    private long userId;

    private UserService userService;

    private List<Reoccurring> reoccurringPayments;

    public ReoccurringController(UserService userService) {
        this.userService = userService;
    }

    // method that preprocesses and returns reoccurring page
    @GetMapping("/reoccurring")
    public String showReoccurring(Model theModel, Principal principal) {
        // retrieve user information
        user = userService.findByUserName(principal.getName());
        userId = user.getId();
        // retrieve reoccurring payments
        reoccurringPayments = userService.findReoccurringPaymentsById(userId);

        if(reoccurringPayments == null) {
            reoccurringPayments = new ArrayList<>();
        }

        // list of reoccurring payments that have been processed for view display.
        List<WebReoccurring> webReoccurring = new ArrayList<>();
        // iterate through each reoccurring payment
        for (Reoccurring p: reoccurringPayments) {
            // retrieve payment dates of current reoccurring payment
            LocalDateTime newRenewDate = p.getRenewDate().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
            LocalDateTime newLastPaymentDate = p.getDateLastRenewed().toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
            // process each payment if  payment pending due date is pending
            while (newRenewDate.isBefore(LocalDateTime.now())) {

                // create a corresponding transaction
                Transaction newTransaction = new Transaction(p.getName(),1, p.getExpense(), p.getEssential(), p.getType(), Timestamp.valueOf(newRenewDate), p.getAmount(), user);

                // process transaction
                if(newTransaction.getExpense()==1) {
                    // if checking account has insufficient funds, flag payment, otherwise process transaction
                    if(!userService.updateUserCheckingAccount(user, -newTransaction.getAmount())) {
                        p.setLastPaymentFailed(true);
                    } else {
                        userService.saveTransaction(newTransaction);
                        p.setLastPaymentFailed(false);
                    }
                } else {
                    userService.updateUserCheckingAccount(user, newTransaction.getAmount());
                    userService.saveTransaction(newTransaction);
                }
                // update renew date and last payment date.
                newRenewDate = newRenewDate.plusMonths(1);
                newLastPaymentDate = newLastPaymentDate.plusMonths(1);

            }
            // update reoccurring payment in database.
            p.setRenewDate(Timestamp.valueOf(newRenewDate));
            p.setDateLastRenewed(Timestamp.valueOf(newLastPaymentDate));
//            userService.saveReoccurring(p);
            String expense = "expense";
            String essential = "essential";
            // configure display information
            if (p.getExpense() == 0) {
                expense = "income";
            }

            if (p.getEssential() == 0) {
                essential = "non-essential";
            }
            userService.updateReoccurring(p);
            // add processed reoccurring payment to be displayed to view
            webReoccurring.add(new WebReoccurring(p.getName(), p.getAmount(), p.getRenewDate().toLocalDateTime(), p.getDateLastRenewed().toLocalDateTime(), p.getId(), expense, essential, p.getType(),p.isLastPaymentFailed()));
        }
        // add configured payments to the model
        theModel.addAttribute("payments", webReoccurring);
        // return reoccurring html page
        return "reoccurring/reoccurring";
    }

    // method that process the deletion of a reoccurring payment given a payment id
    @GetMapping("/processReoccurringDelete")
    private String processReoccurringDelete(@RequestParam("reoccurringId") long id, Principal principal, Model theModel) {
        // retrieve user information
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();

        // find payment
        Reoccurring r = userService.findReoccurringById(id);

        // process if payment exists
        if (r != null) {
            // if user does not have permission to delete payment redirect them to reoccurring page.
            if (r.getUser().getId() != user.getId()) {
                return "redirect:/gfinance/reoccurring";
            }
            // delete payment
            userService.deleteReoccurringById(id);
        }

        return "redirect:/gfinance/reoccurring";
    }

    // method that displays reoccurring payment form
    @GetMapping("/reoccurringForm")
    private String showReoccurringForm(Model theModel, Principal principal) {
        user = userService.findByUserName(principal.getName());
        userId = user.getId();

        theModel.addAttribute("reoccurring", new WebReoccurring());

        return "reoccurring/reoccurring-form";
    }


    // method that processes a newly submitted reoccurring payment
    @PostMapping("/processReoccurringForm")
    private String processReoccurringForm(@Validated @ModelAttribute("reoccurring") WebReoccurring reoccurring, BindingResult theBindingResult,
                                          Model theModel, Principal principal) {

        // if form submission has errors, return form
        if (theBindingResult.hasErrors()) {
            return"reoccurring/reoccurring-form";
        }

        // create new reoccurring  payment
        String date = reoccurring.getRenewDate();
        user = userService.findByUserName(principal.getName());
        userId = user.getId();
        String[] array = date.split("-");
        int year = Integer.valueOf(array[0]);
        int month = Integer.valueOf(array[1]);
        int day = Integer.valueOf(array[2]);
        LocalDate next = LocalDate.of(year, month, day);
        LocalDate last = next.minusMonths(1);
        int expense = 1;
        int essential = 1;
        if (reoccurring.getExpense().equals("income")) {
            expense = 0;
        }

        if (reoccurring.getEssential().equals("non-essential")) {
            essential = 0;
        }

        LocalTime t = LocalTime.of(0,0);
        Reoccurring r = new Reoccurring(reoccurring.getAmount(), expense, essential, reoccurring.getType(),
                reoccurring.getName(),
                Timestamp.valueOf(LocalDateTime.of(next, t)),
                        Timestamp.valueOf(LocalDateTime.of(last, t)),
                user);

        // save new reoccurring payment
        userService.saveReoccurring(r);
        return "redirect:/gfinance/reoccurring";
    }
}

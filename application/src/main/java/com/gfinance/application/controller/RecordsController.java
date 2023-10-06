package com.gfinance.application.controller;

import com.gfinance.application.entity.Checking;
import com.gfinance.application.entity.Reoccurring;
import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebDate;
import com.gfinance.application.user.WebReoccurring;
import com.gfinance.application.user.WebTransaction;
import com.gfinance.application.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
// controller that handles the records page logic and endpoint management.
@Controller
@RequestMapping("/gfinance")
public class RecordsController {

    private long userId;
    private UserService userService;

    private WebDate dateObject;
    private LocalDateTime searchDate = null;

    private String dateDisplay;

    private List<Transaction> transactions;
    public RecordsController(UserService userService) {
        this.userService = userService;
    }

    // method that preprocesses and returns records page displaying user transactions
    @GetMapping("/records")
    public String getRecords(Principal principal, Model theModel) {
        // retrieve user information
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



        // process transactions to be displayed
        Checking userAccount = userService.findCheckingByUserId(userId);
        user.setChecking(userAccount);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // process transactions over a given date period
        if (dateObject != null) {
            if (this.dateObject.getName().equals("day")) {
                // daily transactions processing
                transactions = userService.findTransactionByUserIdAndDay(userId, searchDate);
                dateDisplay = formatter.format(searchDate).toString();
            } else if (this.dateObject.getName().equals("week")) {
                // weekly transactions processing
                transactions = userService.findTransactionByUserIdAndWeek(userId, searchDate);
                LocalDateTime startOfWeek = searchDate;
                LocalDateTime endOfWeek = startOfWeek.plusDays(6);
                String s = formatter.format(startOfWeek);
                String e = formatter.format(endOfWeek);
                dateDisplay = s.toString() + " - " + e.toString();
            } else {
                // monthly transactions processing
                transactions = userService.findTransactionByUserIdAndMonth(userId, searchDate);
                dateDisplay = searchDate.getMonth().toString() + " " + searchDate.getYear();
            }

        } else {
            searchDate = LocalDateTime.now();
            transactions = userService.findTransactionByUserIdAndDay(userId, searchDate);
            dateDisplay = formatter.format(searchDate).toString();
        }


        dateDisplay = "Transactions: " + dateDisplay;

        // append transactions and corresponding time period  date display to view model
        theModel.addAttribute("transactions", transactions);
        theModel.addAttribute("dateDisplay", dateDisplay);
        theModel.addAttribute("date", new WebDate());
        theModel.addAttribute("account", user.getChecking());

        // return records html page
        return "records";

    }

    // method to display records form with the required view model objects
    @GetMapping("/recordsForm")
    public String getTransactionForm(Model theModel, Principal principal) {
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();
        theModel.addAttribute("date", new WebDate());
        theModel.addAttribute("transaction", new WebTransaction());

        return "records-form";
    }


    // method to process records form submission of new transaction
    @PostMapping("/processRecordsForm")
    public String processTransactionForm(@Valid @ModelAttribute("date") WebDate date, BindingResult theBindingResult,
                                         Model theModel, Principal principal){
        User user = userService.findByUserName(principal.getName());

        // return error to view if there are constraint violations for any form inputs.
        if (theBindingResult.hasErrors()) {

            theModel.addAttribute("transactions", transactions);
            theModel.addAttribute("dateDisplay", dateDisplay);

            theModel.addAttribute("account", user.getChecking());
            return "records";
        }

        // redirect to records page
        this.dateObject = date;
        this.searchDate = date.processDate();
        return "redirect:/gfinance/records";
    }


    // process transaction deletion given a transaction id
    @GetMapping("/processDelete")
    public String processDelete(@RequestParam("transactionId") int transactionId, Principal principal) {
        // retrieve user details and transaction to be deleted
        User user = userService.findByUserName(principal.getName());

        Transaction t = userService.findTransactionById(transactionId);
        // delete transaction if exists
        if (t != null) {
            // check if the transaction user corresponds to the active user, if not return to records page
            if (t.getUser().getId() != user.getId()) {
                return "redirect:/gfinance/records";
            }


            // restore account balance if possible
            if(t.getExpense()==1) {
                userService.updateUserCheckingAccount(user, t.getAmount());
            } else {
                if(userService.updateUserCheckingAccount(user, -t.getAmount())) {
                    System.out.println("Deleting transaction successful but leaves checking account negative so no balance restoration");
                }
            }

            // delete transaction
            userService.deleteTransactionById(transactionId);
        }


        // redirect to transaction page
        return "redirect:/gfinance/records";
    }


    // method that proccess new transaction form submission
    @PostMapping("/processNewTransaction")
    public String processNewTransaction(@Valid @ModelAttribute("transaction") WebTransaction transaction, BindingResult theBindingResult,
                                        Model theModel, Principal principal) {

        // check if form submission has errors, if so return form page with errors (done automatically by spring validation)
        if (theBindingResult.hasErrors()) {
            theModel.addAttribute("date", new WebDate());
            return "records-form";
        }

        // retreive user information
        User user = userService.findByUserName(principal.getName());
        userId = user.getId();

        // create new transaction based on submitted info
        Transaction newTransaction = new Transaction();
        newTransaction.setName(transaction.getName());
        newTransaction.setRecurring(0);
        if (transaction.getExpense().equals("expense")) {
            newTransaction.setExpense(1);
        } else {
            newTransaction.setExpense(0);
        }

        if (transaction.getEssential().equals("essential")) {
            newTransaction.setEssential(1);
        } else {
            newTransaction.setEssential(0);
        }

        newTransaction.setTransactionType(transaction.getTransactionType());

        newTransaction.setDate(Timestamp.valueOf(LocalDateTime.now()));

        newTransaction.setAmount(transaction.getAmount());

        newTransaction.setUser(user);


        // check if user has sufficient funds and process transaction appropriately
        if(newTransaction.getExpense()==1) {
            if(!userService.updateUserCheckingAccount(user, -newTransaction.getAmount())) {
                System.out.println("Transaction failed: insufficient funds");
                theModel.addAttribute("insufficientFundsError", "Insufficient funds");
                theModel.addAttribute("date", new WebDate());
                return "records-form";
            }
            userService.saveTransaction(newTransaction);
        } else {
            userService.updateUserCheckingAccount(user, newTransaction.getAmount());
            userService.saveTransaction(newTransaction);
        }


        // redisplay records page
        return "redirect:/gfinance/records";
    }



}

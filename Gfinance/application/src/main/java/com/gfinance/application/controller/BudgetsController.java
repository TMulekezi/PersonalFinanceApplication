package com.gfinance.application.controller;

import com.gfinance.application.entity.Checking;
import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebDate;
import jakarta.validation.Valid;
import org.slf4j.helpers.CheckReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//controller that handles the budget page logic and endpoint management.
@Controller
@RequestMapping("/gfinance")
public class BudgetsController {
    private User user;
    private long userId;

    private UserService userService;

    private List<Transaction> currentDayTransactions;
    private List<Transaction> currentWeekTransactions;

    private List<Transaction> currentMonthTransactions;

    private LocalDateTime day;

    private LocalDateTime week;

    private LocalDateTime month;

    private boolean dayCondition;

    private boolean weekCondition;

    private boolean monthCondition;

    private String dayDisplay;

    private String weekDisplay;

    private String monthDisplay;

    private  double[] dayArray;

    private  double[] weekArray;

    private  double[] monthArray;
    @Autowired
    public BudgetsController(UserService userService) {
        this.userService = userService;


        day = LocalDateTime.now();
        week = LocalDateTime.now();
        month = LocalDateTime.now();

        dayCondition = true;
        weekCondition = false;
        monthCondition = false;
    }

    // method for budgets page endpoint
    @GetMapping("/budgets")
    public String showBudgets(Model theModel, Principal principal) {

        // retrieve user info
        user = userService.findByUserName(principal.getName());
        userId = user.getId();

        // initialize daily, weekly, and monthly transactions
        if (currentDayTransactions==null) {
            currentDayTransactions = userService.findOneTimeTransactionsByUserIdAndDay(userId, LocalDateTime.now());
        }

        if (currentWeekTransactions==null) {
            currentWeekTransactions = userService.findOneTimeTransactionsByUserIdAndWeek(userId, LocalDateTime.now());
        }

        if (currentMonthTransactions==null) {
            currentMonthTransactions = userService.findOneTimeTransactionsByUserIdAndMonth(userId, LocalDateTime.now());
        }

        // calculate and format budget breakdown for each period
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // day
        dayDisplay = "Daily expenditure breakdown: " + formatter.format(day);
        // week
        LocalDateTime startOfWeek = week.minusDays((LocalDateTime.now().getDayOfWeek().getValue()-1));
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        String s = formatter.format(startOfWeek);
        String e = formatter.format(endOfWeek);
        weekDisplay = "Weekly expenditure breakdown: " + s + " - " + e;
        // month
        monthDisplay = "Monthly expenditure breakdown: " + month.getMonth() + " " + month.getYear();



        theModel.addAttribute("dayDisplay", dayDisplay);
        theModel.addAttribute("weekDisplay", weekDisplay);
        theModel.addAttribute("monthDisplay", monthDisplay);

        dayArray =transactionsBreakDown(currentDayTransactions);
        weekArray =transactionsBreakDown(currentWeekTransactions);
        monthArray =transactionsBreakDown(currentMonthTransactions);
        theModel.addAttribute("dayArray", dayArray);
        theModel.addAttribute("weekArray", weekArray);
        theModel.addAttribute("monthArray", monthArray);

        theModel.addAttribute("dateDay", new WebDate());
        theModel.addAttribute("dateWeek", new WebDate());
        theModel.addAttribute("dateMonth", new WebDate());

        theModel.addAttribute("dayCondition", dayCondition);
        theModel.addAttribute("weekCondition", weekCondition);
        theModel.addAttribute("monthCondition", monthCondition);


        // add info to view model
        theModel.addAttribute("checking", user.getChecking());


        dayArray = null;
        weekArray = null;
        monthArray = null;
        currentDayTransactions = null;
        currentWeekTransactions = null;
        currentMonthTransactions = null;
        dayDisplay = null;
        weekDisplay = null;
        monthDisplay = null;

        // return budgets html page to be displayed
        return "budgets/budgets";
    }

    // method to process budget break down fo a given day
    @PostMapping("/processBudgetDay")
    public String processBudgetDay(@Valid @ModelAttribute("dateDay")WebDate date, BindingResult theBindingResult, Model theModel, Principal principal) {
        // retireve user information
        user = userService.findByUserName(principal.getName());
        // set period to displayed on page load
        dayCondition = true;
        weekCondition = false;
        monthCondition = false;

        // handle form submission errors and return page with error notifications
        if (theBindingResult.hasErrors()) {
            theModel.addAttribute("dayDisplay", dayDisplay);
            theModel.addAttribute("weekDisplay", weekDisplay);
            theModel.addAttribute("monthDisplay", monthDisplay);

            theModel.addAttribute("dayArray", dayArray);
            theModel.addAttribute("weekArray", weekArray);
            theModel.addAttribute("monthArray", monthArray);


            theModel.addAttribute("dateWeek", new WebDate());
            theModel.addAttribute("dateMonth", new WebDate());

            theModel.addAttribute("dayCondition", dayCondition);
            theModel.addAttribute("weekCondition", weekCondition);
            theModel.addAttribute("monthCondition", monthCondition);

            theModel.addAttribute("checking", user.getChecking());
            return "budgets/budgets";
        }

        // retrieve daily budget break down
        LocalDateTime tempDate = date.processDate();
        day = tempDate;

        userId = user.getId();
        currentDayTransactions = userService.findOneTimeTransactionsByUserIdAndDay(userId, tempDate);



        return "redirect:/gfinance/budgets";
    }

    // method to process budget break down fo a given day
    @PostMapping("/processBudgetWeek")
    public String processBudgetWeek(@Valid @ModelAttribute("dateWeek")WebDate date, BindingResult theBindingResult, Model theModel, Principal principal) {
        //retrieve user information
        user = userService.findByUserName(principal.getName());
        // set period to displayed on page load
        dayCondition = false;
        weekCondition = true;
        monthCondition = false;

        // handle errors in form submission
        if (theBindingResult.hasErrors()) {
            theModel.addAttribute("dayDisplay", dayDisplay);
            theModel.addAttribute("weekDisplay", weekDisplay);
            theModel.addAttribute("monthDisplay", monthDisplay);

            theModel.addAttribute("dayArray", dayArray);
            theModel.addAttribute("weekArray", weekArray);
            theModel.addAttribute("monthArray", monthArray);

            theModel.addAttribute("dateDay", new WebDate());

            theModel.addAttribute("dateMonth", new WebDate());

            theModel.addAttribute("dayCondition", dayCondition);
            theModel.addAttribute("weekCondition", weekCondition);
            theModel.addAttribute("monthCondition", monthCondition);

            theModel.addAttribute("checking", user.getChecking());
            // redirect to page to display form errors to users
            return "budgets/budgets";
        }

        // retrieve weekly budget break down
        LocalDateTime tempDate = date.processDate();
        week = tempDate;
        userId = user.getId();

        currentWeekTransactions = userService.findOneTimeTransactionsByUserIdAndWeek(userId, tempDate);


        // redirect to budgets endpoint to display new break down
        return "redirect:/gfinance/budgets";
    }

    // method to process budget break down fo a given day
    @PostMapping("/processBudgetMonth")
    public String processBudgetMonth(@Valid @ModelAttribute("dateMonth")WebDate date, BindingResult theBindingResult, Model theModel,   Principal principal) {
        // retrieve user info
        user = userService.findByUserName(principal.getName());
        // set period to displayed on page load
        dayCondition = false;
        weekCondition = false;
        monthCondition = true;
        // handle form submission errors
        if (theBindingResult.hasErrors()) {

            theModel.addAttribute("dayDisplay", dayDisplay);
            theModel.addAttribute("weekDisplay", weekDisplay);
            theModel.addAttribute("monthDisplay", monthDisplay);

            theModel.addAttribute("dayArray", dayArray);
            theModel.addAttribute("weekArray", weekArray);
            theModel.addAttribute("monthArray", monthArray);

            theModel.addAttribute("dateDay", new WebDate());
            theModel.addAttribute("dateWeek", new WebDate());


            theModel.addAttribute("dayCondition", dayCondition);
            theModel.addAttribute("weekCondition", weekCondition);
            theModel.addAttribute("monthCondition", monthCondition);

            theModel.addAttribute("checking", user.getChecking());
            return "budgets/budgets";
        }

        // retrieve monthly budget break down
        LocalDateTime tempDate = date.processDate();
        month = tempDate;

        userId = user.getId();

        currentMonthTransactions = userService.findOneTimeTransactionsByUserIdAndMonth(userId, tempDate);



        return "redirect:/gfinance/budgets";
    }


    // display form for budget target update
    @GetMapping("/showBudgetForm")
    public String showBudgetForm(Principal principal, Model theModel) {
        user = userService.findByUserName(principal.getName());
        userId = user.getId();

        theModel.addAttribute("checking", user.getChecking());
        return "budgets/budgets-form";
    }

    // process form submission of budget update
    @PostMapping("/processBudgetForm")
    public String processBudgetForm(@Valid @ModelAttribute("checking") Checking checking, BindingResult theBindingResult,
                                    Model theModel, Principal principal) {
        user = userService.findByUserName(principal.getName());
        userId = user.getId();
        user.getChecking().setBudget(checking.getBudget());
        userService.updateUser(user);
        return "redirect:/gfinance/budgets";
    }


    // method that breakdowns spending from a list of transactions and returns the breakdown as an array of info
    private static double[] transactionsBreakDown(List<Transaction> list) {

        double[] breakdown = new double[10];
        double totalSum = 0;
        double essentialSum = 0;
        double nonessentialSum = 0;
        double billsCount =0;
        double leisureCount = 0;
        double transportCount = 0;
        double transferCount = 0;
        double shoppingCount = 0;
        double foodCount = 0;
        double otherCount = 0;

        double count = 0;

        if (list == null) {
            list = new ArrayList<>();
        }

        for (Transaction t: list) {
            if (t.getExpense() == 1) {
                if (t.getEssential() == 1) {
                    essentialSum += t.getAmount();
                } else {
                    nonessentialSum += t.getAmount();
                }
                totalSum += t.getAmount();

                if (t.getTransactionType().equals("bills")) {
                    billsCount += t.getAmount();
                } else if (t.getTransactionType().equals("leisure")) {
                    leisureCount +=t.getAmount();
                } else if (t.getTransactionType().equals("other")) {
                    otherCount += t.getAmount();
                } else if (t.getTransactionType().equals("transport")) {
                    transportCount += t.getAmount();
                } else if (t.getTransactionType().equals("transfer")) {
                    transferCount += t.getAmount();
                } else if (t.getTransactionType().equals("shopping")) {
                    shoppingCount += t.getAmount();
                } else if (t.getTransactionType().equals("food")) {
                    foodCount+=t.getAmount();
                }
            }
        }

        breakdown[0] = totalSum;
        breakdown[1] = essentialSum;
        breakdown[2] = nonessentialSum;
        breakdown[3] = billsCount;
        breakdown[4] = leisureCount;
        breakdown[5] = transportCount;
        breakdown[6] = transferCount;
        breakdown[7] = shoppingCount;
        breakdown[8] = foodCount;
        breakdown[9] = otherCount;

        return breakdown;
    }
}

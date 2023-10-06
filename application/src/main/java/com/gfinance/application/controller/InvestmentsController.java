package com.gfinance.application.controller;

import com.gfinance.application.entity.Investment;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.service.FinanceService;
import com.gfinance.application.user.WebInvestment;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
// controller that handles the investments page logic and endpoint management.
@Controller
@RequestMapping("/gfinance")
public class InvestmentsController {


    private UserService userService;

    private FinanceService financeService;

    private User user;
    private long id;

    private List<Investment> userInvestments;



    public InvestmentsController(UserService userService, FinanceService financeService) {
        this.userService = userService;
        this.financeService = financeService;
    }

    // method that preprocesses and returns investments page
    @GetMapping("/investments")
    public String showInvestments(Model theModel, Principal principal) {
        // retrieve user information
        this.user = userService.findByUserName(principal.getName());
        this.id = user.getId();

        // retrieve user investments
        userInvestments = userService.findInvestmentsByUserId(this.id);


        ArrayList<WebInvestment> userWebInvestments =null;

        // process investments for display to view
        if (userInvestments != null) {
            userWebInvestments = new ArrayList<>();
            for (Investment i : userInvestments) {


                    if(!financeService.setWebInvestment(i.getSymbol(), i.getId())) {
                        // if api limit exceeded, display error to user
                        theModel.addAttribute("apiError", "Api rate limit exceeded");
                        break;
                    }
                    userWebInvestments.add(financeService.getWebInvestment());


            }
        }

        // append investments to view model
        theModel.addAttribute("investments", userWebInvestments);
        userInvestments = null;
        // return investments page
        return "investments/investment";
    }


    // method that handles the deletion of an investment given an investment id
    @GetMapping("/processInvestmentDelete")
    public String processDelete(@RequestParam("investmentId") int investmentId, Principal principal) {
        // retrieve user information
        User user = userService.findByUserName(principal.getName());
        id = user.getId();

        // retrieve investment to be deleted
        Investment i = userService.findInvestmentById(investmentId);


        // delete if investment is found
        if (i != null) {
            // check if the investment user corresponds to the active user, if not return to records page
            if (i.getUser().getId() != user.getId()) {

                return "redirect:/gfinance/investments";
            }

            userService.deleteInvestment(i);
        }


        // redirect to investments page
        return "redirect:/gfinance/investments";
    }

    // method to display investments form
    @GetMapping("/showInvestmentForm")
    public String showInvestmentForm(Model theModel) {
        theModel.addAttribute("webInvestment", new WebInvestment());
        return "investments/investment-form";
    }

    // method that processes the creation of a new investment
    @PostMapping("/processInvestmentForm")
    public String processBudgetDay(@Valid @ModelAttribute("webInvestment") WebInvestment webInvestment, Model model, Principal principal) {
        // retrieve user info
        User user = userService.findByUserName(principal.getName());
        id = user.getId();

        // create new investment
        Investment newInvestment = new Investment(webInvestment.getSymbol(), user);

        // check if investment exists, if not return error and inform user
        if(!financeService.hasTicker(webInvestment.getSymbol())) {
            model.addAttribute("err", "ticker symbol does not exist");
            return "investments/investment-form";
        }

        // save investment to database
        userService.saveInvestment(newInvestment);

        // redirect to investments page
        return "redirect:/gfinance/investments";
    }
}

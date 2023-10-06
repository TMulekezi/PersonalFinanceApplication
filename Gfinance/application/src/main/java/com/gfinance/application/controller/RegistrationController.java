package com.gfinance.application.controller;

import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// controller that handles user registration logic and endpoints
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService=userService;
    }


    // method to display registration form
    @GetMapping("/showMyRegistrationPage")
    public String showRegistrationPage(Model theModel) {
        theModel.addAttribute("webUser", new WebUser());
        return "sign-up-form";
    }

    // method that processes newly created user
    @PostMapping("/processRegistrationForm")
    public String processRegistrationFrom(@Valid @ModelAttribute("webUser") WebUser theWebUser, BindingResult theBindingResult,
                                          HttpSession session, Model theModel) {

        // check if input form had errors and return form if so with automatically attached errors (handled by Spring validation)
        if (theBindingResult.hasErrors()) {
            return "sign-up-form";
        }

        // check if user already exits, return form if so with error message
        User userPresent = userService.findByUserName(theWebUser.getUserName());
        if (userPresent != null) {
            theModel.addAttribute("webUser", new WebUser());
            theModel.addAttribute("registrationError", "User name already exists");
            return "sign-up-form";
        }

        // save the new user to database a return registration confirmation page
        userService.save(theWebUser);


        return "registration-confirmation";

    }

}

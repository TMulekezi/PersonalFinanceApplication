package com.gfinance.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// controller that handles the login page logic and endpoint management.
@Controller
public class LoginController {

    // method that displays welcome page of application
    @GetMapping("/")
    public String showHomePage() {
        return "homepage";
    }

    // method that displays the login page
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "login-form";
    }


    // method that returns access denied page when application security is breached.
    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}

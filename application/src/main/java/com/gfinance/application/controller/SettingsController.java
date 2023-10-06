package com.gfinance.application.controller;

import com.gfinance.application.entity.Investment;
import com.gfinance.application.entity.Transaction;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebInvestment;
import com.gfinance.application.user.WebSettingsUser;
import com.gfinance.application.user.WebUser;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
// controller that handles logic and endpoints for the settings page.
@Controller
@RequestMapping("/gfinance")
public class SettingsController {
    private UserService userService;

    private User user;
    private long userId;
    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    // method that preprocess and returns settings page
    @GetMapping("/settings")
    public String showSettings(Principal principal, Model theModel) {
        user = userService.findByUserName(principal.getName());
        userId = user.getId();
        theModel.addAttribute("user", new User());
        theModel.addAttribute("webUser", new WebSettingsUser());


        theModel.addAttribute("userCheck", user);
        return "settings/settings";
    }


    // method that facilitates the user self account deletion
    @GetMapping("/processUserDelete")
    public String processUserDelete(@RequestParam("checkId") long checkId, Principal principal) {
        user = userService.findByUserName(principal.getName());
        userId = user.getId();
        if (checkId != userId) {
            return "redirect:/gfinance/settings";
        }
        userService.deleteUser(user);
        return "redirect:/showMyLoginPage";
    }

    // method that facilitates the admin search for user
    @PostMapping("/processUserSettingsForm")
    public String processBudgetDay(@Valid @ModelAttribute("webUser") WebSettingsUser webUser, Model model, Principal principal) {
        User searchUser = userService.findByUserName(webUser.getUserName());

        model.addAttribute("webUser", new WebSettingsUser());

        model.addAttribute("userCheck", user);
        if (searchUser == null) {
            model.addAttribute("err", "the user does not exist");

            model.addAttribute("user", new User());
            return "settings/settings";
        }



        model.addAttribute("user", searchUser);
        return "settings/settings";
    }

    // method that facilitates the admin deletion of user
    @GetMapping("/processAdminUserDelete")
    public String processDelete(@RequestParam("username") String username, Principal principal) {
        user = userService.findByUserName(principal.getName());
        User userToDelete = userService.findByUserName(username);
        if (user.getRoles().get(0).getRolename().equals("ROLE_ADMIN")) {
            userService.deleteUser(userToDelete);
        }

        return "redirect:/gfinance/settings";
    }

    // method that facilitates the admin enabling/disabling of user
    @GetMapping("/processAdminUserActivation")
    public String processActivation(@RequestParam("username") String username, Principal principal, Model theModel) {
        user = userService.findByUserName(principal.getName());
        User userToActivate = userService.findByUserName(username);
        if (user.getRoles().get(0).getRolename().equals("ROLE_ADMIN")) {
            if (userToActivate.getEnabled() == 1) {
                userToActivate.setEnabled(0);
            } else {
                userToActivate.setEnabled(1);
            }
        }

        userService.updateUser(userToActivate);
        theModel.addAttribute("user", userToActivate);
        theModel.addAttribute("webUser", new WebSettingsUser());
        theModel.addAttribute("userCheck", user);
        return "settings/settings";
    }
}

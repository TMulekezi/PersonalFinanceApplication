package com.gfinance.application.controller;

import com.gfinance.application.entity.Achievement;
import com.gfinance.application.entity.User;
import com.gfinance.application.service.UserService;
import com.gfinance.application.user.WebAchievement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
//controller that handles the achievement page logic and endpoint management.
@Controller
@RequestMapping("/gfinance")
public class AchievementsController {

    private UserService userService;

    private User user;

    private long userId;

    private List<Achievement> budgetAchievements;

    private List<Achievement> savingsAchievements;

    public AchievementsController(UserService userService) {
        this.userService = userService;
    }

    // endpoint method to display all of users achievements
    @GetMapping("/achievements")
    public String showAchievements(Model theModel, Principal principal) {
        // retrieve user info
        user = userService.findByUserName(principal.getName());
        userId = user.getId();

        // retrieve achievemets
        if (budgetAchievements == null) {
            budgetAchievements = userService.findUserBudgetAchievementsById(userId);
            savingsAchievements = userService.findUserSavingsAchievementsById(userId);
        }

        // format achievements for web display

        List<WebAchievement> budgetWebAchievements = new ArrayList<>();
        List<WebAchievement> savingsWebAchievements = new ArrayList<>();

        if (budgetAchievements == null) {
            budgetAchievements = new ArrayList<>();
        }

        if (savingsAchievements == null) {
            savingsAchievements = new ArrayList<>();
        }

        for (Achievement achievement: budgetAchievements) {
            budgetWebAchievements.add(new WebAchievement(achievement, user.getBudgetStreak()));
        }

        for (Achievement achievement: savingsAchievements) {
            savingsWebAchievements.add(new WebAchievement(achievement, user.getSavingsStreak()));
        }


        // add them to view model
        theModel.addAttribute("savingsAchievements",savingsWebAchievements);
        theModel.addAttribute("budgetAchievements", budgetWebAchievements);

        // return html page
        return "achievements/achievements";
    }
}

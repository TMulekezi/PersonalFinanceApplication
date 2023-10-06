package com.gfinance.application.user;

import com.gfinance.application.entity.Achievement;
import com.gfinance.application.entity.BudgetStreak;
import com.gfinance.application.entity.SavingsStreak;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.swing.*;
// Achievement object used to process Entity achievements retrieved from a database for view display
public class WebAchievement {




    private String type;


    private String name;

    private String icon;


    private int target;

    private int progress;

    private double progressPercentage;

    public WebAchievement(Achievement achievement, BudgetStreak budgetStreak) {
        type = achievement.getType();
        name = achievement.getName();
        icon = achievement.getAchievementIcon();
        target = achievement.getTarget();
        progress = budgetStreak.getRecordStreak();
        progressPercentage = ((double)progress/(double)target) * 100;
        if (progressPercentage > 100) {
            progressPercentage = 100;
        }

    }

    public WebAchievement(Achievement achievement, SavingsStreak savingsStreak) {
        type = achievement.getType();
        name = achievement.getName();
        icon = achievement.getAchievementIcon();
        target = achievement.getTarget();
        progress = savingsStreak.getRecordStreak();
        progressPercentage = ((double) progress/(double) target) * 100;
        if (progressPercentage > 100) {
            progressPercentage = 100;
        }

    }

    public  WebAchievement() {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    @Override
    public String toString() {
        return "WebAchievement{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", target=" + target +
                ", progress=" + progress +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}

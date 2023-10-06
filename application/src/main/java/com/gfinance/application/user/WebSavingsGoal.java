package com.gfinance.application.user;

import com.gfinance.application.entity.Savings;
import com.gfinance.application.entity.SavingsGoal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

// Savings goal object used to process Entity saving goals retrieved from a database for view display
@Validated
public class WebSavingsGoal {

    private long id;

    // spring validation criteria
    @NotNull(message = "is required")
    private String name;

    @NotNull(message = "is required")
    private  double amount;

    private double progress;

    public WebSavingsGoal(){

    }

    public WebSavingsGoal(SavingsGoal goal, Savings savingsAccount, long id) {
        this.id = id;
        name = goal.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        amount = goal.getAmount();
        progress = (savingsAccount.getAmount()/goal.getAmount()) * 100;
        if (progress > 100) {
            progress = 100;
        }

    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getId() {
       return id;
    }

    public void setId(long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "WebSavingsGoal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +

                ", progress=" + progress +
                '}';
    }
}

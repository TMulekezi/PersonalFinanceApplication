package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
// Savings transaction object used to process savings transactions retrieved from a database for view display
@Validated
public class WebSavingsTransaction {

    // spring validation criteria
    @NotNull(message = "is required")
    private String type;

    @NotNull
    private double amount;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WebSavingsTransaction{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}

package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
// Transaction object used to process Entity transactions retrieved from a database for view display
@Validated
public class WebTransaction {


    // spring validation criteria
    @NotNull(message = "is required")
    private String expense;

    @NotNull(message = "is required")
    private String name;

    @NotNull(message = "is required")
    private String essential;


    private String transactionType;



    @NotNull
    private double amount;


    public String getEssential() {
        return essential;
    }

    public void setEssential(String essential) {
        this.essential = essential;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WebTransaction{" +
                "expense='" + expense + '\'' +
                ", essential='" + essential + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                '}';
    }
}

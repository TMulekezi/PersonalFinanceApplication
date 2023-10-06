package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Reoccurring payment object used to process Entity reoccurring payments retrieved from a database for view display
@Validated
public class WebReoccurring {

    private long id;

    // spring validation criteria
    @NotNull(message = "required")
    private String name;

    @NotNull(message = "required")
    private double amount;


    @NotNull(message = "required")
    private String renewDate;

    @NotNull(message = "required")
    private String expense;

    @NotNull(message = "required")
    private String essential;

    @NotNull(message = "required")
    private String type;

    private String lastDateRenewed;

    private boolean failed;
    public WebReoccurring() {

    }

    public WebReoccurring(String name, double amount, LocalDateTime renewDate, LocalDateTime lastDateRenewed, long id, String expense,
                          String essential, String type, Boolean failed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.name = name;
        this.amount = amount;
        this.renewDate = formatter.format(renewDate);
        this.lastDateRenewed = formatter.format(lastDateRenewed);
        this.id = id;
        this.expense = expense;
        this.essential = essential;
        this.type = type;
        this.failed = failed;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(String renewDate) {
        this.renewDate = renewDate;
    }

    public String getLastDateRenewed() {
        return lastDateRenewed;
    }

    public void setLastDateRenewed(String lastDateRenewed) {
        this.lastDateRenewed = lastDateRenewed;
    }

    public String getEssential() {
        return essential;
    }

    public void setEssential(String essential) {
        this.essential = essential;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "WebReoccurring{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", renewDate='" + renewDate + '\'' +
                ", lastDateRenewed='" + lastDateRenewed + '\'' +
                '}';
    }
}

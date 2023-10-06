package com.gfinance.application.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="reoccurring")
public class Reoccurring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="amount")
    private double amount;

    @Column(name = "expense")
    private int expense;

    @Column(name = "essential")
    private int essential;

    @Column(name = "type")
    private String type;

    @Column(name="name")
    private String name;

    @Column(name="renew_date")
    private Timestamp renewDate;

    @Column(name="date_last_renewed")
    private Timestamp dateLastRenewed;

    @Column(name = "last_payment_failed")
    private boolean lastPaymentFailed;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public Reoccurring() {

    }

    public Reoccurring(double amount, int expense, int essential, String type, String name, Timestamp renewDate, Timestamp dateLastRenewed, User user) {
        this.amount = amount;
        this.expense = expense;
        this.essential = essential;
        this.type = type;
        this.name = name;
        this.renewDate = renewDate;
        this.dateLastRenewed = dateLastRenewed;
        this.user = user;
    }

    public int getEssential() {
        return essential;
    }

    public void setEssential(int essential) {
        this.essential = essential;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(Timestamp renewDate) {
        this.renewDate = renewDate;
    }

    public Timestamp getDateLastRenewed() {
        return dateLastRenewed;
    }

    public void setDateLastRenewed(Timestamp dateLastRenewed) {
        this.dateLastRenewed = dateLastRenewed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLastPaymentFailed() {
        return lastPaymentFailed;
    }

    public void setLastPaymentFailed(boolean lastPaymentFailed) {
        this.lastPaymentFailed = lastPaymentFailed;
    }

    @Override
    public String toString() {
        return "Reoccurring{" +
                "id=" + id +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", renewDate=" + renewDate +
                ", dateLastRenewed=" + dateLastRenewed +
                '}';
    }
}

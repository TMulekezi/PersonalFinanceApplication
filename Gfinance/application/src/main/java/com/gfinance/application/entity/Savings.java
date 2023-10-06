package com.gfinance.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name="savings")
public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="amount")
    private double amount;

    @Column(name="savings_target")
    private double savings_target;

    @Column(name="savings_contribution")
    private double savingsContribution;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public Savings() {

    }

    public Savings(double amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public Savings(User user) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getSavings_target() {
        return savings_target;
    }

    public void setSavings_target(double savings_target) {
        this.savings_target = savings_target;
    }

    public double getSavingsContribution() {
        return savingsContribution;
    }

    public void setSavingsContribution(double savingsContribution) {
        this.savingsContribution = savingsContribution;
    }
}

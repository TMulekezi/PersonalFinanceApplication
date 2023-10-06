package com.gfinance.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name="checking")
public class Checking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="amount")
    private double amount;

    @Column(name="budget")
    private double budget;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public Checking(){

    }

    public Checking(double amount, User user, double budget) {
        this.amount = amount;
        this.user = user;
        this.budget = budget;
    }

    public Checking(User user) {
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Checking{" +
                "id=" + id +
                ", amount=" + amount +
                ", budget=" + budget +
                '}';
    }
}

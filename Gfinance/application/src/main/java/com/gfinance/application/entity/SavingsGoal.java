package com.gfinance.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name="savings_goal")
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="amount")
    private double amount;

    @Column(name="name")
    private String name;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public SavingsGoal(double amount, String name, int id) {
        this.amount = amount;
        this.name = name;
        this.id = id;
    }

    public SavingsGoal(double amount, String name) {
        this.amount = amount;
        this.name = name;
        this.id = id;
    }

    public SavingsGoal() {

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SavingsGoal{" +
                "id=" + id +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                '}';
    }
}

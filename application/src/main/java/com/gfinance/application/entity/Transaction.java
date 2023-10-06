package com.gfinance.application.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="name")
    private String name;

    @Column(name = "recurring")
    private int recurring;

    @Column(name ="expense")
    private int expense;

    @Column(name = "essential")
    private int essential;

    @Column(name ="type")
    private String transactionType;


    @Column(name="date")
    private Timestamp date;

    @Column(name="amount")
    private double amount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction() {}


    public Transaction(String name, int recurring, int expense, int essential, String transactionType, Timestamp date, double amount) {
        this.recurring = recurring;
        this.expense=expense;
        this.essential = essential;
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.name = name;
    }

    public Transaction(String name,int recurring, int expense, int essential, String transactionType, Timestamp date, double amount, User u) {
        this.recurring = recurring;
        this.expense=expense;
        this.essential = essential;
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.user = u;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecurring() {
        return recurring;
    }

    public void setRecurring(int recurring) {
        this.recurring = recurring;
    }

    public int getEssential() {
        return essential;
    }

    public void setEssential(int essential) {
        this.essential = essential;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", recurring=" + recurring +
                ", expense=" + expense +
                ", essential=" + essential +
                ", transactionType='" + transactionType + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", user=" + user +
                '}';
    }
}

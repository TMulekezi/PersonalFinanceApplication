package com.gfinance.application.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;


    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="enabled")
    private int enabled;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_achievements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private List<Achievement> achievements;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Checking checking;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Savings savings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BudgetStreak budgetStreak;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SavingsStreak savingsStreak;



    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<Reoccurring> reoccurringPayments;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<SavingsGoal> savingsGoals;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<Investment> investments;


    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }

        roles.add(role);
    }

    public User() {

    }

    public User(String username, String password, int enabled, List<Role> roles, Checking checking, Savings savings, BudgetStreak budgetStreak, SavingsStreak savingsStreak, List<Transaction> transactions, List<SavingsGoal> savingsGoals, List<Investment> investments) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.checking = checking;
        this.savings = savings;
        this.budgetStreak = budgetStreak;
        this.savingsStreak = savingsStreak;
        this.transactions = transactions;
        this.savingsGoals = savingsGoals;
        this.investments = investments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Checking getChecking() {
        return checking;
    }

    public void setChecking(Checking checking) {
        this.checking = checking;
    }

    public Savings getSavings() {
        return savings;
    }

    public void setSavings(Savings savings) {
        this.savings = savings;
    }

    public BudgetStreak getBudgetStreak() {
        return budgetStreak;
    }

    public void setBudgetStreak(BudgetStreak budgetStreak) {
        this.budgetStreak = budgetStreak;
    }

    public SavingsStreak getSavingsStreak() {
        return savingsStreak;
    }

    public void setSavingsStreak(SavingsStreak savingsStreak) {
        this.savingsStreak = savingsStreak;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<SavingsGoal> getSavingsGoals() {
        return savingsGoals;
    }

    public void setSavingsGoals(List<SavingsGoal> savingsGoals) {
        this.savingsGoals = savingsGoals;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(List<Investment> investments) {
        this.investments = investments;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +

                '}';
    }




}

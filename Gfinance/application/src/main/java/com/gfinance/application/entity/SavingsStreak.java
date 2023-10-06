package com.gfinance.application.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "savings_streak")
public class SavingsStreak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;


    @Column(name="record_streak")
    private int recordStreak;

    @Column(name="current_streak")
    private int currentStreak;

    @Column(name="date_last_confirmed")
    private Timestamp dateLastConfirmed;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public SavingsStreak() {

    }

    public SavingsStreak(int recordStreak, int currentStreak, Timestamp dateLastConfirmed, User user) {
        this.recordStreak = recordStreak;
        this.currentStreak = currentStreak;
        this.dateLastConfirmed = dateLastConfirmed;
        this.user = user;
    }

    public SavingsStreak(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecordStreak() {
        return recordStreak;
    }

    public void setRecordStreak(int recordStreak) {
        this.recordStreak = recordStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Timestamp getDateLastConfirmed() {
        return dateLastConfirmed;
    }

    public void setDateLastConfirmed(Timestamp dateLastConfirmed) {
        this.dateLastConfirmed = dateLastConfirmed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SavingsStreak{" +
                "id=" + id +
                ", recordStreak=" + recordStreak +
                ", currentStreak=" + currentStreak +
                ", dateLastConfirmed=" + dateLastConfirmed +
                "}";
    }
}

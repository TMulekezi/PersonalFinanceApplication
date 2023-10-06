package com.gfinance.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "achievement_name")
    private String name;

    @Column(name = "achievement_icon")
    private String achievementIcon;

    @Column(name="target")
    private int target;

    public Achievement() {

    }

    public String getAchievementIcon() {
        return achievementIcon;
    }

    public void setAchievementIcon(String achievementIcon) {
        this.achievementIcon = achievementIcon;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}

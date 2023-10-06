package com.gfinance.application.dao;

import com.gfinance.application.entity.Achievement;

import java.util.List;

public interface AchievementDao {
    List<Achievement> findAchievements();
}

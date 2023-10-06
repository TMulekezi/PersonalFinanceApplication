package com.gfinance.application.dao;

import com.gfinance.application.entity.SavingsGoal;

public interface SavingsGoalDao {
    void save(SavingsGoal goal);

    void deleteSavingsGoalById(long id);

    SavingsGoal findGoalById(long id);
}

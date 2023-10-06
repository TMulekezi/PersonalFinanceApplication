package com.gfinance.application.dao;

import com.gfinance.application.entity.Checking;

public interface CheckingDao {
    boolean updateCheckingAmountById(long id, double amount);

    Checking findCheckingByUserId(long id);
}

package com.gfinance.application.dao;

import com.gfinance.application.entity.Investment;

public interface InvestmentDao {
    void deleteInvestment(Investment investment);

    Investment findInvestmentById(long id);

    void saveInvestment(Investment i);
}

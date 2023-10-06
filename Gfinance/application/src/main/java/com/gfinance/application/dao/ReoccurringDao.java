package com.gfinance.application.dao;

import com.gfinance.application.entity.Reoccurring;
import com.gfinance.application.entity.Transaction;

public interface ReoccurringDao {
    void deleteReoccurringPaymentById(long id);

    void saveReoccurringPayment(Reoccurring r);

    void updateReoccurringPayment(Reoccurring r);

    Reoccurring findReoccurringById(long id);
}

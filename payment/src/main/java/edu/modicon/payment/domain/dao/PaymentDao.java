package edu.modicon.payment.domain.dao;

import edu.modicon.payment.domain.model.Payment;

public interface PaymentDao {

    void makeAPay(Payment payment);
}

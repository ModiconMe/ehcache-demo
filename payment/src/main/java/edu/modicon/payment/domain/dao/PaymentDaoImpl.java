package edu.modicon.payment.domain.dao;

import edu.modicon.payment.domain.model.Payment;
import edu.modicon.payment.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentDaoImpl implements PaymentDao {

    private final PaymentRepository paymentRepository;

    @Override
    public void makeAPay(Payment payment) {
        paymentRepository.save(payment);
    }
}

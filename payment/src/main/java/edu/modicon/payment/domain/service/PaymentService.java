package edu.modicon.payment.domain.service;

import edu.modicon.payment.application.dto.CustomerDto;
import edu.modicon.payment.domain.dao.PaymentDao;
import edu.modicon.payment.domain.model.Payment;
import edu.modicon.payment.infrastructure.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentDao paymentDao;

    @JmsListener(destination = "${spring.activemq.queue}", containerFactory = "jmsListenerQueueContainerFactory")
    public void makeAPay(CustomerDto customerDto) {
        log.info("Received message: " + customerDto);

        Payment payment = Payment.builder()
                .customerEmail(customerDto.email())
                .amount(BigDecimal.valueOf(109))
                .build();

        log.info("Create payment: " + payment);
    }
}

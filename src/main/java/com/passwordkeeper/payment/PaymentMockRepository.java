package com.passwordkeeper.payment;

import com.passwordkeeper.utils.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentMockRepository {

    public Payment getPaymentInfo(String paymentId) {
        return new Payment(paymentId, PaymentStatus.PAYED);
    }

    public String requestPayment() {
        return IdGenerator.generateId();
    }
}

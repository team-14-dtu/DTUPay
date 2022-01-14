package db;

import rest.Payment;
import rest.User;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentHistory {

    final static Set<Payment> paymentHistory = new HashSet<>(
            Arrays.asList(
                new Payment("paymentId", "merchantId", "customerId", BigDecimal.valueOf(100), "description"),
                new Payment("paymentId1", "merchantId1", "customerId1", BigDecimal.valueOf(101), "description1"),
                new Payment("paymentId2", "merchantId2", "customerId2", BigDecimal.valueOf(102), "description2")
            )
    );

    public Payment getTargetPayment(String paymentId) {
        Payment targetPayment = null;
        for (Payment payment : paymentHistory) {
            if (payment.getId().equals(paymentId)) {
                targetPayment = payment;
                break;
            }
        }
        return targetPayment;
    }

    public List<Payment> getPaymentsForUser(String userId, User.Type type) {
        return paymentHistory.stream().filter(p -> matchesType(p, userId, type)).collect(Collectors.toList());
    }

    public void addPaymentHistory(Payment payment) {
        paymentHistory.add(payment);
    }

    private static boolean matchesType(Payment payment, String id, User.Type type) {
        switch (type) {
            case CUSTOMER:
                return payment.getDebtorId().equals(id);
            case MERCHANT:
                return payment.getCreditorId().equals(id);
            default:
                return true;
        }
    }


}

package db;

import rest.Payment;
import rest.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentHistory {

    final static List<Payment> paymentHistory = new ArrayList<>(
            Arrays.asList(new Payment("pid", "mid", "cid", "100", "description", false))
    );

    public List<Payment> getAllPayments() {
        return paymentHistory;
    }

    public Payment getTargetPayment(String paymentId) {
        List<Payment> payments = paymentHistory.stream().filter(p -> p.getId().equals(paymentId)).collect(Collectors.toList());
        return payments.get(0);
    }

    public List<Payment> getPaymentsForUser(String userId, User.Type type) {
        return paymentHistory.stream().filter(p -> matchesType(p, userId, type)).collect(Collectors.toList());
    }

    public void setPaymentHistory(String paymentId, String customerId, String merchantId, String amount, String description) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setDebtorId(customerId);
        payment.setCreditorId(merchantId);
        payment.setAmount(amount);
        payment.setDescription(description);
        payment.setLocal(false);
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

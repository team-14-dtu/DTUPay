package db;

import rest.Payment;
import rest.User;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentHistory {

    private static final UUID uuid1 = UUID.randomUUID();
    private static final UUID uuid2 = UUID.randomUUID();
    private static final UUID uuid3 = UUID.randomUUID();

    final static Map<UUID, Payment> paymentHistory = new HashMap<UUID, Payment>() {{
        put(uuid1, new Payment(uuid1, "token1", "merchantId1", "customerId1", BigDecimal.valueOf(101), "description1"));
        put(uuid2, new Payment(uuid2, "token2", "merchantId2", "customerId2", BigDecimal.valueOf(102), "description2"));
        put(uuid3, new Payment(uuid3, "token3", "merchantId3", "customerId3", BigDecimal.valueOf(103), "description3"));
    }};

    public Payment getTargetPayment(UUID paymentId) {
        Payment targetPayment = null;
        for (UUID uuid : paymentHistory.keySet()) {
            if (uuid.equals(paymentId)) {
                targetPayment = paymentHistory.get(uuid);
                break;
            }
        }
        return targetPayment;
    }

    public List<Payment> getPaymentsForUser(String userId, User.Type type) {
        return paymentHistory.values().stream().filter(payment -> matchesType(payment, userId, type)).collect(Collectors.toList());
    }

    public void addPaymentHistory(Payment payment) {
        paymentHistory.put(payment.getId(), payment);
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

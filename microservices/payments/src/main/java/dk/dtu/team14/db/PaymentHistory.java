package dk.dtu.team14.db;

import event.payment.history.ReplyPaymentHistory;
import rest.User;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentHistory {

    final static Map<String, Payment> paymentHistory = new HashMap<String, Payment>() {{
        put("uuid1", new Payment("uuid1", "merchantId1", "customerId1", BigDecimal.valueOf(101), "description1"));
        put("uuid2", new Payment("uuid2", "merchantId2", "customerId2", BigDecimal.valueOf(102), "description2"));
        put("uuid3", new Payment("uuid3", "merchantId3", "customerId3", BigDecimal.valueOf(103), "description3"));
    }};

    public Payment getTargetPayment(UUID paymentId) {
        Payment targetPayment = null;
        for (String uuid : paymentHistory.keySet()) {
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

    public List<ReplyPaymentHistory> getHistory(String userId, User.Type type) {
        List<Payment> fullPaymentHistory = paymentHistory.values().stream().filter(payment -> matchesType(payment, userId, type)).collect(Collectors.toList());
        List<ReplyPaymentHistory> historyList = new ArrayList<>();
        for (Payment payment : fullPaymentHistory) {
            historyList.add(new ReplyPaymentHistory(payment.getId(), payment.getAmount(), payment.getDescription()));
        }
        return historyList;
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
            case MANAGER:
                return true;
            default:
                return false;
        }
    }


}

package services.db;

import services.data.Payment;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class PaymentHistory {

    private Map<UUID, Payment> paymentHistory = new HashMap<>();

    public List<PaymentHistoryCustomer> getCustomerHistory(UUID customerId) { //Manager history = full history
        List<PaymentHistoryCustomer> customerHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            if (payment.getValue().getCustomerId().equals(customerId)) {
                customerHistory.add(new PaymentHistoryCustomer(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp(), payment.getValue().getMerchantName()));
            }
        }
        return customerHistory;
    }

    public List<PaymentHistoryMerchant> getMerchantHistory(UUID merchantId) { //Manager history = full history
        List<PaymentHistoryMerchant> merchantHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            if (payment.getValue().getMerchantId().equals(merchantId)) {
                merchantHistory.add(new PaymentHistoryMerchant(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp()));
            }
        }
        return merchantHistory;
    }

    public List<PaymentHistoryManager> getManagerHistory() { //Manager history = full history
        List<PaymentHistoryManager> managerHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            managerHistory.add(new PaymentHistoryManager(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp(), payment.getValue().getMerchantName(), payment.getValue().getCustomerId(), payment.getValue().getMerchantId(), payment.getValue().getCustomerName()));
        }
        return managerHistory;
    }

    public void addPaymentHistory(UUID paymentId, Payment payment) {
        paymentHistory.put(paymentId, payment);
    }


}

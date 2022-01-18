package dk.dtu.team14.db;

import dk.dtu.team14.data.Payment;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class PaymentHistory {

    private Map<UUID, Payment> paymentHistory = new HashMap<UUID, Payment>(){{
            put(UUID.randomUUID(), new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100), "description", new Timestamp(System.currentTimeMillis())));
    }};
    /*
    private UUID customerId;
    private UUID merchantId;
    private BigDecimal amount;
    private String description;
    private Timestamp timeStamp;
     */

    public List<PaymentHistoryCustomer> getCustomerHistory(UUID merchantId) { //Manager history = full history
        List<PaymentHistoryCustomer> customerHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            if (payment.getValue().getMerchantId().equals(merchantId)) {
                customerHistory.add(new PaymentHistoryCustomer(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp(), "Firstmerchant Lastmerchant"));
            }
        }
        return customerHistory;
    }

    public List<PaymentHistoryMerchant> getMerchantHistory(UUID customerId) { //Manager history = full history
        List<PaymentHistoryMerchant> merchantHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            if (payment.getValue().getMerchantId().equals(customerId)) {
                merchantHistory.add(new PaymentHistoryMerchant(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp()));
            }
        }
        return merchantHistory;
    }

    public List<PaymentHistoryManager> getManagerHistory() { //Manager history = full history
        List<PaymentHistoryManager> managerHistory = new ArrayList<>();
        for (Map.Entry<UUID, Payment> payment : paymentHistory.entrySet()) {
            managerHistory.add(new PaymentHistoryManager(payment.getKey(), payment.getValue().getAmount(), payment.getValue().getDescription(), payment.getValue().getTimeStamp(), "Firstmerchant Lastmerchant", UUID.randomUUID(), UUID.randomUUID(), "Firstcustomer Lastcustomer"));
        }
        return managerHistory;
    }

    public void addPaymentHistory(UUID paymentId, Payment payment) {
        paymentHistory.put(paymentId, payment);
    }


}

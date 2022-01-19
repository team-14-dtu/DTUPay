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

//    private UUID uuidCustomer1 = new UUID(new BigInteger(("0f14d0ab-9605-4a62-a9e4-5ed26688389b".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("0f14d0ab-9605-4a62-a9e4-5ed26688389b".replace("-", "")).substring(16), 16).longValue());
//    private UUID uuidCustomer2 = new UUID(new BigInteger(("0da48506-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("0da48506-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(16), 16).longValue());
//    private UUID uuidCustomer3 = new UUID(new BigInteger(("1a0a3ca0-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("1a0a3ca0-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(16), 16).longValue());
//    private UUID uuidMerchant1 = new UUID(new BigInteger(("1e9e0f12-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("1e9e0f12-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(16), 16).longValue());
//    private UUID uuidMerchant2 = new UUID(new BigInteger(("247a4f5e-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("247a4f5e-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(16), 16).longValue());
//    private UUID uuidMerchant3 = new UUID(new BigInteger(("2c3ec602-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger(("2c3ec602-78b1-11ec-90d6-0242ac120003".replace("-", "")).substring(16), 16).longValue());

//    private Map<UUID, Payment> paymentHistory = new HashMap<UUID, Payment>(){{
//        put(UUID.randomUUID(), new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(75), "Ice cream @ Kongens Nytorv", new Timestamp(System.currentTimeMillis()), "Default Customer", "Default Merchant"));
//        put(UUID.randomUUID(), new Payment(uuidCustomer1, uuidMerchant1, BigDecimal.valueOf(230), "Beers @ Minetta Tavern", new Timestamp(System.currentTimeMillis()), "Søren Tønnesen", "Sammuel L. Jackson"));
//        put(UUID.randomUUID(), new Payment(uuidCustomer2, uuidMerchant2, BigDecimal.valueOf(4500), "Rent", new Timestamp(System.currentTimeMillis()), "Keanu Reeves", "Taylor Swift"));
//        put(UUID.randomUUID(), new Payment(uuidCustomer3, uuidMerchant3, BigDecimal.valueOf(25000), "Carpenter work for summer house", new Timestamp(System.currentTimeMillis()), "David Samões", "Ed Sheeran"));
//        put(UUID.randomUUID(), new Payment(uuidCustomer2, uuidMerchant1, BigDecimal.valueOf(150), "Used shoes", new Timestamp(System.currentTimeMillis()), "Keanu Reeves", "Sammuel L. Jackson"));
//    }};

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

package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryManager extends PaymentHistoryCustomer {
    UUID customerId;
    UUID merchantId;
    String customerName;

    public PaymentHistoryManager(UUID paymentId, BigDecimal amount, String description, Timestamp timestamp, String merchantName, UUID customerId, UUID merchantId, String customerName) {
        super(paymentId, amount, description, timestamp, merchantName);
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.customerName = customerName;
    }
}

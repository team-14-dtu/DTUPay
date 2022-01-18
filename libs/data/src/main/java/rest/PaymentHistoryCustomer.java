package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryCustomer extends PaymentHistoryMerchant {
    String merchantName;

    public PaymentHistoryCustomer(UUID paymentId, BigDecimal amount, String description, Timestamp timestamp, String merchantName) {
        super(paymentId, amount, description, timestamp);
        this.merchantName = merchantName;
    }
}

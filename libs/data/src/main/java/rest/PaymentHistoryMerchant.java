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
public class PaymentHistoryMerchant {
    private UUID paymentId;
    private BigDecimal amount;
    private String description;
    private Timestamp timestamp;
}

package services.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Payment {
    private UUID customerId;
    private UUID merchantId;
    private BigDecimal amount;
    private String description;
    private Timestamp timeStamp;
    private String customerName;
    private String merchantName;
}

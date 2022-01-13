package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    public String id;
    public String creditorId;
    public String debtorId;
    public BigDecimal amount;
    public String description;
}

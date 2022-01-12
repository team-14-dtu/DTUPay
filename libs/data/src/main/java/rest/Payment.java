package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    public String id;
    public String creditorId;
    public String debtorId;
    public int amount;
    public String description;
    public boolean isLocal;


}

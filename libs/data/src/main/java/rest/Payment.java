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
    public String amount;
    public String description;
    public boolean isLocal; //TODO delete this field

}

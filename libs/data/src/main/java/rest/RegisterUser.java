package rest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUser {
    private String bankAccountId;
    private String name;
    private String cpr;
    private Boolean isMerchant;
}

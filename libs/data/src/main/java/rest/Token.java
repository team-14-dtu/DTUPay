package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
public class Token {
    public String customerID;
    public String tokenString;

    public Token(String customerID) {
        this.customerID = customerID;
        this.tokenString = (new Random()).toString();
    }
}

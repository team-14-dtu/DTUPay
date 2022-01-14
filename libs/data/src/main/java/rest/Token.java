package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Token {
    public UUID customerID;
    public UUID tokenString;

    public Token(UUID customerID) {
        this.customerID = customerID;
        this.tokenString = UUID.randomUUID();
    }
}

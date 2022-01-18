package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Token {
    public UUID customerId;
    public UUID tokenId;

    public Token(UUID customerId) {
        this.customerId = customerId;
        this.tokenId = UUID.randomUUID();
    }
}

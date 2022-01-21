package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// @author : Mathilde
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokensRequested {
    public UUID customerId;
    public int numberOfTokens;
}
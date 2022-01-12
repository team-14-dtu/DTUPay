package rest;

import java.util.Random;

public class Token {
    public String customerID;
    public String tokenString;

    public Token(String customerID) {
        this.customerID = customerID;
        this.tokenString = (new Random()).toString();
    }
}

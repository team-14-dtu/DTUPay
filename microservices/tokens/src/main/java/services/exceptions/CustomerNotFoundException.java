package services.exceptions;

// @author : Naja
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String error) {
        super(error);
    }
}

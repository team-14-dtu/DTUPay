package services.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String error) {
        super(error);
    }
}

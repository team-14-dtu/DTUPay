package services.exceptions;

// @author : Mathilde
public class CanNotGenerateTokensException extends Exception {
    public CanNotGenerateTokensException(String errorMessage) {
        super(errorMessage);
    }
}

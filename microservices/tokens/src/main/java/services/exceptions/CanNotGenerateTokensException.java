package services.exceptions;

public class CanNotGenerateTokensException extends Exception {
    public CanNotGenerateTokensException(String errorMessage) {
        super(errorMessage);
    }
}

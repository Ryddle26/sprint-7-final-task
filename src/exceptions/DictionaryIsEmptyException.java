package exceptions;

public class DictionaryIsEmptyException extends RuntimeException {
    public DictionaryIsEmptyException(String message) {
        super(message);
    }
}

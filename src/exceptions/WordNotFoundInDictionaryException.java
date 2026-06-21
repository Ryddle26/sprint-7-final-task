package exceptions;

public class WordNotFoundInDictionaryException extends RuntimeException {
    public WordNotFoundInDictionaryException(String word) {
        super("Слово " + word + " отсутствует в словаре");
    }
}

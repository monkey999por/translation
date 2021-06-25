package translate;

public interface TranslationClient {
    /**
     * translate by text as...
     * 1. text is english -> translate to japanese
     * 2. text is japanese -> translate to english
     *
     * @param text translate text.
     * @return translate result.
     */
    String request(String text);
}

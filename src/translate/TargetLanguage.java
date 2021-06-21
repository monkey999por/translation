package translate;

/**
 * target of translation language code.
 */
public enum TargetLanguage {
    ENGLISH("en"),
    JAPANESE("ja");

    private final String languageCode;

    TargetLanguage(String code) {
        this.languageCode = code;
    }


    public String getLanguageCode() {
        return this.languageCode;
    }
}

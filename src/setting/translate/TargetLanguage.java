package setting.translate;

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

    @Override
    public String toString() {
        return this.languageCode;
    }
}

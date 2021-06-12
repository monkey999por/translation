package setting.translate;

/**
 * target of translation language code.
 */
public enum TargetLanguageCode {
    ENGLISH("en"),
    JAPANESE("ja");

    private String languageCode;

    TargetLanguageCode(String code) {
        this.languageCode = code;
    }

    @Override
    public String toString() {
        return this.languageCode;
    }
}

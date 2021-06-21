package translate;

/**
 * target of translation language code.
 */
public enum TargetLang {
    ENGLISH("en"),
    JAPANESE("ja"),
    KOREA("ko"),
    CHINESE("zh-cn");

    public final String languageCode;

    TargetLang(String code) {
        this.languageCode = code;
    }

}

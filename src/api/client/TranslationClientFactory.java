package api.client;

public class TranslationClientFactory {
    private TranslationClientFactory() {
    }

    ;

    public static TranslationClient newInstance() {
//        return Setting.getAsString("translation_client").equals("deepl") ? new TranslationClientOfDeepL() :
//                new TranslationClientOfGoogleAppScript();
        return new TranslationClientImpl();
    }

}

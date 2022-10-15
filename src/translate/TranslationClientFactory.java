package translate;

import monkey999.tools.Setting;

public class TranslationClientFactory {
    private TranslationClientFactory(){};

    public static TranslationClient newInstance(){
        return Setting.getAsString("translation_client").equals("deepl") ? new TranslationClientOfDeepL() :
                new TranslationClientOfGoogleAppScript();
    }

}

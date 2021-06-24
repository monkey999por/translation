package translate;

import app.Debug;
import app.Setting;
import org.apache.commons.codec.net.URLCodec;
import tools.Cmd;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * see https://script.google.com/home/projects/1sFWfV_MOJbmBeTLT_R9F4Qq0qPolgkRsVt8A_sCMI2C9DTccZBRdHFDt/edit
 */
public class TranslationClientOfGoogleAppScript implements TranslationClient {
    private static final URLCodec codec = new URLCodec("UTF-8");

    private final LangDetector detector = new LangDetectorOfCybozuLabs();

    public TranslationClientOfGoogleAppScript() {
    }

    /**
     * create request URL as google apps script as "google_translate_api".
     *
     * @param text   translate text
     * @param source before language
     * @param target after language
     * @return request URL as google apps script as "google_translate_api"
     */
    public static String createRequestUrl(String text, TargetLang source, TargetLang target) {
        try {
            text = Objects.isNull(text) ? "" : codec.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Debug.print(e);
        }

        String url = Setting.getAsString("translate_request_url");
        return url.replace("{text}", text)
                .replace("{source}", source.languageCode)
                .replace("{target}", target.languageCode);
    }

    /**
     * translate by URL.
     *
     * @param requestUrl created by {@link TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)}.
     * @return translate result.
     */
    public static String request(String requestUrl) {
        return Cmd.execute(false, new String[]{"curl", "-L", "-s", requestUrl});
    }

    @Override
    public String translate(String text) {
        text = Objects.isNull(text) ? "" : text;
        String requestUrl = detector.isJapanese(text)
                ? createRequestUrl(text, TargetLang.JAPANESE, TargetLang.ENGLISH)
                : createRequestUrl(text, TargetLang.ENGLISH, TargetLang.JAPANESE);

        return request(requestUrl);
    }

}

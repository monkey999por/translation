package translate;

import app.Setting;
import com.cybozu.labs.langdetect.LangDetectException;
import org.apache.commons.codec.net.URLCodec;
import tools.Cmd;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * see https://script.google.com/home/projects/1sFWfV_MOJbmBeTLT_R9F4Qq0qPolgkRsVt8A_sCMI2C9DTccZBRdHFDt/edit
 */
public class TranslationClient {
    private static final URLCodec codec = new URLCodec("UTF-8");

    private LangDetector detector = new LangDetectorOfCybozuLabs();

    public TranslationClient() throws LangDetectException {
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
            if (Setting.getAsBoolean("debug_mode")) {
                e.printStackTrace();
            }
        }

        String url = Setting.getAsString("translate_request_url");
        return url.replace("{text}", text)
                .replace("{source}", source.languageCode)
                .replace("{target}", target.languageCode);
    }

    /**
     * translate by URL.
     *
     * @param requestUrl created by {@link TranslationClient#createRequestUrl(String, TargetLang, TargetLang)}.
     * @return translate result.
     */
    public static String request(String requestUrl) {
        return Cmd.execute(false, new String[]{"curl", "-L", "-s", requestUrl});
    }

    /**
     * translate by text as...
     * 1. text is english -> translate to japanese
     * 2. text is japanese -> translate to english
     *
     * @param text translate text.
     * @return translate result.
     * @throws LangDetectException see {@link LangDetectorOfCybozuLabs#isJapanese(String)}
     */
    public static String translate(String text) throws LangDetectException {
        text = Objects.isNull(text) ? "" : text;
        String requestUrl = detector.isJapanese(text)
                ? createRequestUrl(text, TargetLang.JAPANESE, TargetLang.ENGLISH)
                : createRequestUrl(text, TargetLang.ENGLISH, TargetLang.JAPANESE);

        return request(requestUrl);
    }

}

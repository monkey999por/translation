package client;

import com.cybozu.labs.langdetect.LangDetectException;
import common.Cmd;
import common.LangDetector;
import org.apache.commons.codec.net.URLCodec;
import setting.Setting;

import java.io.UnsupportedEncodingException;

/**
 * see https://script.google.com/home/projects/1sFWfV_MOJbmBeTLT_R9F4Qq0qPolgkRsVt8A_sCMI2C9DTccZBRdHFDt/edit
 */
public class TranslationClient {
    static URLCodec codec = new URLCodec("UTF-8");

    /**
     * create request URL as google apps script as "google_translate_api".
     *
     * @param text   translate text
     * @param source before language
     * @param target after language
     * @return request URL as google apps script as "google_translate_api"
     */
    public static String createRequestUrl(String text, String source, String target) {
        try {
            text = codec.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (Boolean.valueOf(Setting.get("debug_mode"))) {
                e.printStackTrace();
            }
        }
        String url = Setting.get("translate_request_url");
        return url.replace("{text}", text)
                .replace("{source}", source)
                .replace("{target}", target);
    }

    /**
     * translate by URL.
     *
     * @param requestUrl created by {@link TranslationClient#createRequestUrl(String, String, String)}.
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
     * @throws LangDetectException
     */
    public static String translate(String text) throws LangDetectException {
        String requestUrl = LangDetector.isJapanese(text)
                ? TranslationClient.createRequestUrl(text, "ja", "en")
                : TranslationClient.createRequestUrl(text, "en", "ja");

        return request(requestUrl);
    }

}

package translate;

import app.Debug;
import constant.AppConst;
import monkey999.tools.Setting;
import org.apache.commons.codec.net.URLCodec;
import monkey999.tools.Cmd;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * see https://script.google.com/home/projects/1sFWfV_MOJbmBeTLT_R9F4Qq0qPolgkRsVt8A_sCMI2C9DTccZBRdHFDt/edit
 */
public class TranslationClientOfGoogleAppScript implements TranslationClient {


    private final LangDetector detector = LangDetectorFactory.newInstance();

    public TranslationClientOfGoogleAppScript() {
        if(Debug.debug_mode()){
            System.out.println("create instance TranslationClientOfGoogleAppScript");
        }
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
            text = Objects.isNull(text) ? "" : AppConst.codec.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Debug.print(e);
        }

        String url = Setting.getAsString("translate_request_url");
        String param = Setting.getAsString("translate_request_param")
                .replace("{text}", text)
                .replace("{source}", source.languageCode)
                .replace("{target}", target.languageCode);
        return new StringBuilder().append(url).append("?").append(param).toString();
    }

    /**
     * translate by URL.
     *
     * @param requestUrl created by {@link TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)}.
     * @return translate result.
     */
    public static String translate(String requestUrl) {
        try {
            return Cmd.execute(false, new String[]{"curl", "-L", "-s", requestUrl});
        }catch (Exception e) {
            // とりあえずつぶしとけ
            return "API ERROR";
        }
    }

    @Override
    public String request(String text) {
        text = Objects.isNull(text) ? "" : text;
        String requestUrl = detector.isJapanese(text)
                ? createRequestUrl(text, TargetLang.JAPANESE, TargetLang.ENGLISH)
                : createRequestUrl(text, TargetLang.ENGLISH, TargetLang.JAPANESE);

        return translate(requestUrl);
    }

}

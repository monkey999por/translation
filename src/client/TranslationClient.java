package client;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.net.URLCodec;

import com.cybozu.labs.langdetect.LangDetectException;

import common.Cmd;
import common.LangDetecter;
import setting.Setting;

public class TranslationClient {
	static URLCodec codec = new URLCodec("UTF-8");

	/**
	 * @param param
	 * @return 翻訳リクエストURL
	 */
	public static String createRequestUrl(String text, String source, String target) {
		try {
			text = codec.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = Setting.get("translate_request_url");
		return url.replace("{text}", text)
				.replace("{source}", source)
				.replace("{target}", target);
	}
	
	public static String request(String requestUrl) {
		return Cmd.execute(false, new String[] { "curl", "-L", "-s", requestUrl });
	}
	public static String translate(String text) throws LangDetectException {
		String requestUrl = LangDetecter.isJapanese(text)
				? TranslationClient.createRequestUrl(text, "ja", "en")
				: TranslationClient.createRequestUrl(text, "en", "ja");

		return request(requestUrl);
	}
	
	
}

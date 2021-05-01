import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.net.URLCodec;

import com.cybozu.labs.langdetect.LangDetectException;

import setting.Setting;

public class Main {

	//クリップボード
	static Toolkit kit = Toolkit.getDefaultToolkit();
	static Clipboard clip = kit.getSystemClipboard();

	static URLCodec codec = new URLCodec("UTF-8");
	//前回のクリップボードの内容を保持する
	static String lastTimeClipText = getClipboardText() == null ? "" : getClipboardText();

	public static void main(String[] args) {
		Setting.load(args[0]);
		String langDetecterProfile = Setting.get("lang_detecter_profile");
		Long loopInterval = new Long(Setting.get("loop_interval"));

		// 翻訳者
		LangDetecter detcter = new LangDetecter();
		try {
			detcter.init(langDetecterProfile);
		} catch (LangDetectException e) {
			e.printStackTrace();
		}

		System.out.println(""
				+ "------------------- Hello Translation -------------------\r\n"
				+ "@see: https://github.com/monkey999por/translation.git\r\n"
				+ "@see: https://github.com/monkey999por/translation.git\r\n"
				+ "\r\n"
				+ "■ Lang Detecter Profile: " + langDetecterProfile + "\r\n"
				+ "■ Interval: " + loopInterval + "ms"
				+ "\r\n");

		// クリップボードを監視しながらポーリング
		int gcCount = 0;
		try {
			while (true) {
				if (lastTimeClipText.equals(getClipboardText())) {
					//一定時間経過後にGCする 1800ループごと
					if (gcCount++ > 1800) {
						System.out.println("GC called");
						Runtime.getRuntime().gc();
						gcCount = 0;
					}
					Thread.sleep(loopInterval);
					continue;
				}

				// 翻訳してコンソール表示　同期　日本語系か英語か
				TranslationClient client = new TranslationClient();
				String translatedText = codec.encode(getClipboardText(), "UTF-8");
				String requestUrl = isJapanese(detcter.detect(getClipboardText()))
						? createRequestUrl(translatedText, "ja", "en")
						: createRequestUrl(translatedText, "en", "ja");
				String result = client.runCurl(new String[] { "curl", "-L", "-s", requestUrl });
				//実行結果
				System.out.println("---------------------------------------------------------");
				System.out.println("■ from -> : " + getClipboardText());
				System.out.println("■ to   -> : " + result);
				System.out.println("");

				lastTimeClipText = getClipboardText();
			}
		} catch (InterruptedException | LangDetectException | UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	}

	/**
	 * @return クリップボードに保存されたテキスト情報
	 */
	static String getClipboardText() {
		try {
			return (String) clip.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			return "";
		}
	}

	/**
	 * @param param
	 * @return 翻訳リクエストURL
	 */
	static String createRequestUrl(String text, String source, String target) {
		String url = Setting.get("translate_request_url");
		return url.replace("{text}", text)
				.replace("{source}", source)
				.replace("{target}", target);
	}

	/**
	 * @param detectResult
	 * @return 
	 */
	static Boolean isJapanese(String detectResult) {
		return detectResult.equals("ja") || detectResult.equals("ko") || detectResult.equals("zh-cn");
	}

}

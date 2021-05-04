import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.cybozu.labs.langdetect.LangDetectException;

import client.MyTextToSpeechClient;
import client.TranslationClient;
import common.LangDetecter;
import common.MyClipBoard;
import setting.Setting;

public class Main {

	//前回のクリップボードの内容を保持する
	static String lastTimeClipText = MyClipBoard.getText() == null ? "" : MyClipBoard.getText();

	public static void main(String[] args) throws Exception {
		Setting.load(args[0]);
		LangDetecter.init();
		welcomePrint();

		// クリップボードを監視しながらポーリング
		int gcCount = 0;
		Long loopInterval = new Long(Setting.get("loop_interval"));

		while (true) {
			if (lastTimeClipText.equals(MyClipBoard.getText())) {
				//一定時間経過後にGCする 1800ループごと
				if (gcCount++ > 1800) {
					System.out.println("Garbage Collection called");
					Runtime.getRuntime().gc();
					gcCount = 0;
				}
				Thread.sleep(loopInterval);
				continue;
			}

			/*
			 * クリップボードのテキストから、翻訳。翻訳結果（またはコピーしたテキスト）のオーディオ再生を行う
			 * ・フロー
			 * ■英語をコピー⇒翻訳リクエスト(非同期)
			 * 　　　　　　　⇒コピーしたテキスト(英語)をオーディオ再生(非同期)
			 * 
			 * ■日本語をコピー⇒翻訳リクエスト(非同期※)⇒
			 * 　　　　　　　　　　　　　　　　　　　　  ⇒コピーしたテキスト(英語)をオーディオ再生(非同期)
			 * 
			 */

			// translate clipboard text
			String ct = MyClipBoard.getText();
			Callable<String> translation = new Callable<String>() {
				@Override
				public String call() throws LangDetectException {
					String result = TranslationClient.translate(ct);
					// translate result to console  
					System.out.println("---------------------------------------------------------");
					System.out.println("■ from -> : " + ct);
					System.out.println("■ to   -> : " + result);
					System.out.println("");

					return result;
				}
			};
			// execute translate clipboard text
			ExecutorService translateService = Executors.newCachedThreadPool();
			Future<String> translationResult = translateService.submit(translation);
			translateService.shutdown();

			/*speech to text run and  playback text to speech result
			 * clipbord text is English ->Clipbord text(=English) to speech
			 * clipbord text is javanese -> Translation result(=English) to speech
			 */
			Runnable textToSpeech = new Runnable() {
				@Override
				public void run() {
					try {
						// text to speech request
						MyTextToSpeechClient.request(
								LangDetecter.isJapanese(ct) ? translationResult.get() : ct);
						
						//  play back text to speech result(mp3)
						// see -> setting : "google_cloud_text_to_speech_out_audio_file"
						if (new Boolean(Setting.get("enable_google_cloud_text_to_speech"))) {
							MyTextToSpeechClient.playback();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			// execute text to speech
			ExecutorService textToSpeechService = Executors.newCachedThreadPool();
			textToSpeechService.submit(textToSpeech);
			textToSpeechService.shutdown();

			lastTimeClipText = ct;
		}
	}

	private static void welcomePrint() {
		System.out.println("------------------- Hello Translation -------------------");
		System.out.println("@see: https://github.com/monkey999por/translation.git");
		System.out.println("@see: https://github.com/monkey999por/language-detection.git");
		System.out.println();
		System.out.println("■ Setting: " + Setting.getFilePath());
		Setting.printAll();
		System.out.println();
	}

}

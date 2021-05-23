package worker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.cybozu.labs.langdetect.LangDetectException;

import client.MyTextToSpeechClient;
import client.TranslationClient;
import common.LangDetecter;
import setting.Setting;

/**
 * @author nogam
 * translation worker do ...
 * 1. translation text
 * 2.  translation text playback!!
 * 
 */
public class TranslationWorker {

	public TranslationWorker() {

	}

	/**
	 * @param translationText
	 * クリップボードのテキストから、翻訳。翻訳結果（またはコピーしたテキスト）のオーディオ再生を行う
	 * ・フロー
	 * ■英語をコピー⇒翻訳リクエスト(非同期)
	 * 　　　　　　　⇒コピーしたテキスト(英語)をオーディオ再生(非同期)
	 * 
	 * ■日本語をコピー⇒翻訳リクエスト(非同期※)⇒
	 * 　　　　　　　　　　　　　　　　　　　　  ⇒コピーしたテキスト(英語)をオーディオ再生(非同期)
	 */
	public static void run(String translationText) {
		// translate clipboard text
		Callable<String> translation = new Callable<String>() {
			@Override
			public String call() throws LangDetectException {
				String result = TranslationClient.translate(translationText);
				// translate result to console  
				System.out.println("---------------------------------------------------------");
				System.out.println("■ from -> : " + translationText);
				System.out.println("■ to   -> : " + result);
				System.out.println();
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
							LangDetecter.isJapanese(translationText) ? translationResult.get() : translationText);

					//  play back text to speech result(mp3)
					// see -> setting : "google_cloud_text_to_speech_out_audio_file"
					if (Boolean.valueOf(Setting.get("enable_google_cloud_text_to_speech"))) {
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
	}

}

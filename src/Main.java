import java.io.FileNotFoundException;

import client.MyTextToSpeechClient;
import client.TranslationClient;
import common.LangDetecter;
import common.MyClipBoard;
import javazoom.jl.decoder.JavaLayerException;
import setting.Setting;

public class Main {

	//前回のクリップボードの内容を保持する
	static String lastTimeClipText = MyClipBoard.getText() == null ? "" : MyClipBoard.getText();

	public static void main(String[] args) throws Exception {
		Setting.load(args[0]);
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

			String ct = MyClipBoard.getText();

			// translate text
			String result = TranslationClient.translate(ct);

			// translate result to console  
			System.out.println("---------------------------------------------------------");
			System.out.println("■ from -> : " + ct);
			System.out.println("■ to   -> : " + result);
			System.out.println("");

			// speech to text run
			// clipbord text is English ->Clipbord text(=English) to speech 
			// clipbord text is javanese -> Translation result(=English) to speech
			if (LangDetecter.isJapanese(ct)) {
				MyTextToSpeechClient.request(result);
			} else {
				MyTextToSpeechClient.request(ct);
			}

			// playback text to speech result
			if (new Boolean(Setting.get("enable_google_cloud_text_to_speech"))) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							MyTextToSpeechClient.playback();
						} catch (FileNotFoundException | JavaLayerException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

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

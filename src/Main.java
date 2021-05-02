import client.TranslationClient;
import common.LangDetecter;
import common.MyClipBoard;
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

			// 翻訳してコンソール表示　同期　日本語系か英語か
			String ct = MyClipBoard.getText();
			String requestUrl = LangDetecter.isJapanese(ct)
					? TranslationClient.createRequestUrl(ct, "ja", "en")
					: TranslationClient.createRequestUrl(ct, "en", "ja");

			String result = TranslationClient.request(requestUrl);

			//実行結果
			System.out.println("---------------------------------------------------------");
			System.out.println("■ from -> : " + ct);
			System.out.println("■ to   -> : " + result);
			System.out.println("");

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

import java.util.Scanner;

import common.LangDetecter;
import common.MyClipBoard;
import setting.Setting;
import worker.TranslationWorker;

public class Main {

	//前回のクリップボードの内容を保持する
	static String lastTimeClipText = MyClipBoard.getText() == null ? "" : MyClipBoard.getText();

	public static void main(String[] args) throws Exception {
		Setting.load(args[0]);
		LangDetecter.init();
		welcomePrint();

		System.out.println("コンソールに入力した文字を翻訳します。");
		Scanner scanner = new Scanner(System.in, "utf-8");

		System.out.println(scanner.next());
		// クリップボードを監視しながらポーリング
		int gcCount = 0;
		Long loopInterval = Long.valueOf(Setting.get("loop_interval"));

		System.out.println("クリップボードを監視し、変更があった場合は翻訳します。");
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
			
			// translation worker run
			String ct = MyClipBoard.getText();
			TranslationWorker.run(ct);
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

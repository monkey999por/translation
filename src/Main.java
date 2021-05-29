import common.LangDetector;
import setting.Setting;
import thread.ClipBoardObserver;
import thread.StandardInObserver;

public class Main {
	public static void main(String[] args) throws Exception {
		Setting.load(args[0]);
		LangDetector.init();
		welcomePrint();

		// クリップボードを監視し、変更があった場合は翻訳者(TranslationWorker)を呼び出し 別スレッドで実行
		new Thread(new ClipBoardObserver()).start();

		// 標準入力を監視する。入力があった場合は翻訳者(TranslationWorker)を呼び出し 別スレッドで実行
		new Thread(new StandardInObserver()).start();

	}

	private static void welcomePrint() {
		System.out.println("------------------- Hello Translation -------------------");
		System.out.println("@see: https://github.com/monkey999por/translation.git");
		System.out.println("@see: https://github.com/monkey999por/language-detection.git");
		System.out.println();
		System.out.println("■ Setting: " + Setting.getFilePath());
		Setting.printAll();
		System.out.println();
		System.out.println("■■■機能■■■");
		System.out.println("  ・クリップボードを監視し、変更があった場合は翻訳します。");
		System.out.println("  ・コンソールに入力した文字を翻訳します。");
		System.out.println();
	}

}

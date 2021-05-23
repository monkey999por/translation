package thread;

import common.MyClipBoard;
import setting.Setting;
import worker.TranslationWorker;

public class ClipBoardObserver implements Runnable {

	//前回のクリップボードの内容を保持する
	private static String lastTimeClipText = MyClipBoard.getText() == null ? "" : MyClipBoard.getText();

	public ClipBoardObserver() {
	}

	@Override
	public void run() {
		// クリップボードを監視しながらポーリング
		int gcCount = 0;
		Long loopInterval = Long.valueOf(Setting.get("loop_interval"));

		while (true) {
			if (lastTimeClipText.equals(MyClipBoard.getText())) {
				//一定時間経過後にGCする 1800ループごと
				if (gcCount++ > 1800) {
					System.out.println("Garbage Collection called");
					Runtime.getRuntime().gc();
					gcCount = 0;
				}
				try {
					Thread.sleep(loopInterval);
				} catch (InterruptedException e) {
					if (Boolean.valueOf(Setting.get("debug_mode"))) {
						e.printStackTrace();
					}
				}
				continue;
			}

			// translation worker run
			String ct = MyClipBoard.getText();
			TranslationWorker.run(ct);
			lastTimeClipText = ct;
		}
	}
}

package thread;

import java.util.Scanner;

import setting.Setting;
import worker.TranslationWorker;

public class StandardInObserver implements Runnable {

	public StandardInObserver() {
	}

	@Override
	public void run() {
		try (Scanner scanner = new Scanner(System.in, Setting.get("standard_in_encoding"))) {
			while (true) {
				TranslationWorker.run(scanner.nextLine());
			}
		} catch (Exception e) {
			if (Boolean.valueOf(Setting.get("debug_mode"))) {
				e.printStackTrace();
			}
		}
	}
}

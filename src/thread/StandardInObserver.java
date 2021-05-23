package thread;

import java.util.Scanner;

import worker.TranslationWorker;

public class StandardInObserver implements Runnable {

	public StandardInObserver() {
	}

	@Override
	public void run() {
		try(Scanner scanner = new Scanner(System.in, "utf-8")){
			while (true) {
				TranslationWorker.run(scanner.nextLine());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

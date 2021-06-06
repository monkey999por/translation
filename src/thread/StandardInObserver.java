package thread;

import setting.Setting;
import worker.TaskMediator;
import worker.TaskType;
import worker.TextParser;

import java.util.Scanner;

public class StandardInObserver implements Runnable {

	public StandardInObserver() {
	}

	@Override
	public void run() {
		try (Scanner scanner = new Scanner(System.in, Setting.get("standard_in_encoding"))) {
			while (true) {
				var input = scanner.nextLine();
				if (TextParser.isCommand(input)){
					TaskMediator.order(TaskType.COMMAND, input);
				} else {
					TaskMediator.order(TaskType.TRANSLATE, input);
				}

			}
		} catch (Exception e) {
			if (Boolean.valueOf(Setting.get("debug_mode"))) {
				e.printStackTrace();
			}
		}
	}
}

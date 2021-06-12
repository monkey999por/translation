package thread;

import setting.common.Setting;
import worker.TaskMediator;
import worker.TaskType;
import worker.TextParser;

import java.util.Scanner;

/**
 * Monitor Standard in.
 */
public class StandardInObserver implements Runnable {

    public StandardInObserver() {
    }

    /**
     * Monitor Standard in, and when detect input, run event.
     */
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in, Setting.getAsString("standard_in_encoding"))) {
            while (true) {
                var input = scanner.nextLine();
                if (TextParser.isCommand(input)) {
                    TaskMediator.order(TaskType.COMMAND, input);
                } else {
                    TaskMediator.order(TaskType.TRANSLATE, input);
                }

            }
        } catch (Exception e) {
            if (Setting.getAsBoolean("debug_mode")) {
                e.printStackTrace();
            }
        }
    }
}

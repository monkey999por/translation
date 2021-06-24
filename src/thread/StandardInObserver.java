package thread;

import app.Setting;
import cmd.CommandExecutor;
import translate.TranslationWorker;

import java.util.Scanner;

/**
 * Monitor Standard in.
 */
public class StandardInObserver implements Runnable {

    TranslationWorker worker = new TranslationWorker();

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
                if (CommandExecutor.isCommand(input)) {
                    CommandExecutor.run(input);
                } else {
                    worker.run(input);
                }

            }
        } catch (Exception e) {
            if (Setting.getAsBoolean("debug_mode")) {
                e.printStackTrace();
            }
        }
    }
}

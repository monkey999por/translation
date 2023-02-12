package thread;

import monkey999.tools.Setting;
import tools.Debug;
import tools.cmd.CommandExecutor;

import java.util.NoSuchElementException;
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
        } catch (NoSuchElementException e1) {
            System.out.println("致命的なエラーが発生しました。\r\n" +
                    "コマンドプロンプト上で何も選択していない状態でctrl+cを押下しないでください。\r\n" +
                    "アプリケーションを再起動します。");
            System.exit(-1);
        } catch (Exception e) {
            Debug.print(e);
        }
    }
}

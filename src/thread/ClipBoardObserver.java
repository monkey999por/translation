package thread;

import app.Setting;
import tools.MyClipBoard;
import translate.TranslationWorker;

import java.awt.datatransfer.DataFlavor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Monitor System clipboard.
 */
public class ClipBoardObserver implements Runnable {

    TranslationWorker worker = new TranslationWorker();

    //前回のクリップボードの内容を保持する
    private static Object lastTimeClip = MyClipBoard.get();

    /**
     * Monitor System clipboard, and when detect clipboard changed, run event.
     */
    @Override
    public void run() {

        // クリップボードを監視しながらポーリング
        int gcCount = 0;
        var loopInterval = Setting.getAsLong("loop_interval");

        while (true) {

            var currentClip = MyClipBoard.get();
            if (lastTimeClip != null &&
                currentClip != null &&
                    lastTimeClip.equals(MyClipBoard.get())) {
                //一定時間経過後にGCする 1800ループごと
                if (gcCount++ > 1800) {
                    System.out.println("Garbage Collection called");
                    Runtime.getRuntime().gc();
                    gcCount = 0;
                }
                try {
                    Thread.sleep(loopInterval);
                } catch (InterruptedException e) {
                    if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
                }
                continue;
            }

            // translation worker run
            var ct = (String) MyClipBoard.get(DataFlavor.stringFlavor);
            worker.run(ct);
            lastTimeClip = ct;
        }
    }
}
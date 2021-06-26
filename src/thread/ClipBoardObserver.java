package thread;

import app.Debug;
import app.Setting;
import tools.ClipBoardItem;
import translate.TranslationWorker;

/**
 * Monitor System clipboard.
 */
public class ClipBoardObserver implements Runnable {

    TranslationWorker worker = new TranslationWorker();

    /**
     * Monitor System clipboard, and when detect clipboard changed, run event.
     */
    @Override
    public void run() {

        var gcController = new GCController();
        var loopInterval = Setting.getAsLong("loop_interval");

        var lastTimeClip = new ClipBoardItem();
        var currentClip = new ClipBoardItem();


        while (true) {
            try {
                currentClip = new ClipBoardItem();
                if (lastTimeClip.equal(currentClip)) {
                    // call it a certain number of times, GC will be executed.
                    gcController.call();

                    Thread.sleep(loopInterval);
                    continue;
                }
            } catch (Exception e) {
                Debug.print(e);
                continue;
            }

            // translation worker run
            if (currentClip.isText()) {
                worker.run((String) currentClip.getAsValue());
            }
            lastTimeClip = currentClip;

        }
    }

    private class GCController {
        private int gcCount;
        private final int threshold = 1800;

        /**
         * If you call it a certain number of times, GC will be executed.
         */
        void call() {
            this.gcCount++;

            //一定回数ループ後にGCする 1800ループごと
            if (this.gcCount > threshold) {
                Debug.print("Garbage Collection called");
                Runtime.getRuntime().gc();
                this.gcCount = 0;
            }
        }

    }
}
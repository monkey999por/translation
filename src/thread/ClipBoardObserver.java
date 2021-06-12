package thread;

import common.MyClipBoard;
import setting.common.Setting;
import worker.TaskMediator;
import worker.TaskType;

/**
 * Monitor System clipboard.
 */
public class ClipBoardObserver implements Runnable {

    //前回のクリップボードの内容を保持する
    private static String lastTimeClipText = MyClipBoard.getText() == null ? "" : MyClipBoard.getText();

    public ClipBoardObserver() {
    }

    /**
     * Monitor System clipboard, and when detect clipboard changed, run event.
     */
    @Override
    public void run() {
        // クリップボードを監視しながらポーリング
        int gcCount = 0;
        var loopInterval = Setting.getAsLong("loop_interval");

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
                    if (Setting.getAsBoolean("debug_mode")) {
                        e.printStackTrace();
                    }
                }
                continue;
            }

            // translation worker run
            var ct = MyClipBoard.getText();
            TaskMediator.order(TaskType.TRANSLATE, ct);
            lastTimeClipText = ct;
        }
    }
}

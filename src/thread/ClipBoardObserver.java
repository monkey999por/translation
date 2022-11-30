package thread;

import app.Debug;
import monkey999.tools.Setting;
import tools.ClipBoardItem;
import translate.TranslationWorker;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Monitor System clipboard.
 */
public class ClipBoardObserver implements Runnable {

    TranslationWorker worker = new TranslationWorker();
    Long loopInterval = Setting.getAsLong("loop_interval");


    /**
     * Monitor System clipboard, and when detect clipboard changed, run event.
     */
    @Override
    public void run() {

        var gcController = new GCController();
        var useLevelController = new UseLevelController();

        var lastTimeClip = new ClipBoardItem();
        var currentClip = new ClipBoardItem();

        while (true) {
            try {
                currentClip = new ClipBoardItem();
                if (lastTimeClip.equal(currentClip)) {
                    // call it a certain number of times, GC will be executed.
                    gcController.call();

                    // ユーザ使用レベルを調整。詳細はプロパティ:use_level参照
                    useLevelController.call(false);

                    Thread.sleep(loopInterval);
                    continue;
                } else {
                    useLevelController.call(true);
                }


            } catch (InterruptedException ie) {
                Debug.print("stop clipboard observer");
                break;
            } catch (Exception e) {
                Debug.print(e);
                continue;
            }

            // translation worker run
            if (currentClip.isText()) {
                worker.run((String) currentClip.getAsValue());
                if (Setting.getAsBoolean("save_result")) {

                    try {
                        Thread.sleep(4000L);
                        lastTimeClip = new ClipBoardItem();
                        currentClip = new ClipBoardItem();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            lastTimeClip = currentClip;

        }
    }

    /**
     * ガベージコレクションを意図的に行う。
     * ループ内での使用を想定
     * {@link #call()}の呼び出し回数が1800回を超えたときにGCをコール
     */
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

    /**
     * クリップボードの使用率によって、クリップボード監視ループの間隔を調整するためのクラス。
     * {@link #call(boolean)}の呼び出し回数によってループ間隔を調整する。（呼び出し回数が多ければ間隔は長くなる）
     * 詳細は設定の<code>use_level</code>参照。
     */
    private class UseLevelController {
        private Integer loopCount = Integer.valueOf(0);
        private Map<Integer, Long> useLevels = new LinkedHashMap<>();

        /**
         * use_level初期化用
         */
        void initUseLevel() {
            Arrays.stream(Setting.getAsString("use_level")
                            .split(","))
                    .map(i -> i.split(":"))
                    .forEach(x -> useLevels.put(Integer.parseInt(x[0]), Long.parseLong(x[1])));
        }

        /**
         * @param reset ユーザによるアプリの使用があったか
         */
        void call(boolean reset) {
            if (reset) {
                // 初期値に戻す
                loopInterval = Setting.getAsLong("loop_interval");
                loopCount = 0;
                return;
            }
            initUseLevel();
            this.loopCount++;

            useLevels.forEach((count, interval) -> {
                if (this.loopCount.equals(count)) {
                    Debug.print("\"use_level\" changed. current level: " + interval);
                }
                if (this.loopCount >= count) {
                    loopInterval = interval;
                    return;
                }
            });
        }
    }
}
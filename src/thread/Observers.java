package thread;

import app.Debug;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Observers {

    ExecutorService executor;

    protected static Runnable runner;

    /**
     * thread stop
     */
    public void stop(){
        if (executor != null) {
            while (! executor.isShutdown()) {
                Debug.print("is shutdown" + executor.isShutdown());
                executor.shutdownNow();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            executor = null;
        }
    };

    /**
     * thread start
     * @return
     */
    public void start() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(this.runner);
        } else {
            Debug.print("すでに実行中の監視者がいます");
        }
    }
}

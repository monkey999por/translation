package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Observers {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * thread stop
     * @return success
     */
    public void stop(){
        executor.shutdownNow();
    };

    /**
     * thread start
     * @return
     */
    public void start(Runnable runner) {
        executor.submit(runner);
    }
}

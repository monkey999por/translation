package thread;

import app.Debug;

import javax.sound.sampled.Clip;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClipBoardObservers extends Observers{

    private static ClipBoardObservers observers;

    private ClipBoardObservers(){};
    synchronized public static ClipBoardObservers getInstance(){
        Debug.print(observers == null ? "new instance" : "old instans");
        if (Observers.runner == null) {
            runner = new ClipBoardObserver();
        }
        if (observers == null) {
            observers = new ClipBoardObservers();
        }
        return observers;
    }

}

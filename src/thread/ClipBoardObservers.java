package thread;

import javax.sound.sampled.Clip;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClipBoardObservers extends Observers{

    private static ClipBoardObservers clipBoardObservers;

    private ClipBoardObservers(){};
    synchronized public static ClipBoardObservers getInstance(){
        return clipBoardObservers == null ? new ClipBoardObservers() : clipBoardObservers;
    }

}

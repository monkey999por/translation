package thread;

import tools.Debug;

public class ClipBoardObservers extends Observers {

    private static ClipBoardObservers observers;

    private ClipBoardObservers() {
    }

    ;

    synchronized public static ClipBoardObservers getInstance() {
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

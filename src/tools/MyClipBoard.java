package tools;

import app.Setting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

/**
 * System clipboard wrapper.
 */
public class MyClipBoard {
    //クリップボード
    private static Toolkit kit;
    private static Clipboard clip;

    /**
     * init system clipboard
     */
    private static void init() {
        kit = Toolkit.getDefaultToolkit();
        clip = kit.getSystemClipboard();
    }

    /**
     * get data from saved clipboard.
     * get value by cast of {@link DataFlavor} supported.
     *
     * @return The current clipboard value is the specified flavor to return current clipboard.
     * not is null.
     */
    public static Object get(DataFlavor flavor) {
        try {
            init();
            return clip.isDataFlavorAvailable(flavor) ? clip.getData(flavor) : null;

        } catch (Exception e) {
            if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
            return null;
        }
    }

    /**
     * get current clipboard data.
     *
     * @return type of {@link java.lang.String} or {@link java.awt.Image}.
     */
    public static Object get() {
        try {
            init();
            DataFlavor availableFlavor = Arrays.asList(
                    DataFlavor.stringFlavor,
                    DataFlavor.imageFlavor).stream()
                    .filter(f -> clip.isDataFlavorAvailable(f))
                    .findFirst()
                    .get();

            return clip.getData(availableFlavor);
        } catch (Exception e) {
            if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
            return null;
        }
    }
}

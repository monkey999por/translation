package tools.external;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * System clipboard wrapper.
 */
public class MyClipBoard {
    //クリップボード
    private static final Toolkit kit = Toolkit.getDefaultToolkit();
    private static final Clipboard clip = kit.getSystemClipboard();

    /**
     * get text from saved clipboard.
     *
     * @return text
     */
    public static String getText() {
        try {
            return (String) clip.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            return "";
        }
    }
}

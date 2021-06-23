package tools;

import app.Setting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.Arrays;
import java.util.Optional;

/**
 * hold clipboard item.
 */
public class ClipBoardItem {

    // current clipboard item
    // may be final
    private Optional<Object> item;

    /**
     * init with get current clipboard item
     *
     * @throws Exception can't get current clipboard item
     */
    public ClipBoardItem() {
        try {
            // system clipboard
            Toolkit kit = Toolkit.getDefaultToolkit();
            Clipboard clip = kit.getSystemClipboard();

            DataFlavor availableFlavor = Arrays.asList(
                    DataFlavor.stringFlavor,
                    DataFlavor.imageFlavor).stream()
                    .filter(f -> clip.isDataFlavorAvailable(f))
                    .findFirst()
                    .get();
            item = Optional.ofNullable(clip.getData(availableFlavor));
        } catch (Exception e) {
            item = Optional.empty();
            if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
        }
    }

    /**
     * @return current item as not wrap by {@link Optional}.
     */
    public Object getAsValue() {
        try {
            return item.get();
        } catch (Exception e){
            return  null;
        }

    }

    /**
     * @return current clipboard item is {@link Image} ?
     */
    public boolean isImage() {
        try {
            return item.get() instanceof Image;
        } catch (Exception e){
            if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
            return false;
        }
    }

    /**
     * @return current clipboard item is {@link String} ?
     */
    public boolean isText() {
        try {
            return item.get() instanceof String;
        } catch (Exception e){
            if (Setting.getAsBoolean("debug_mode")) e.printStackTrace();
            return false;
        }
    }

    public boolean isEmpty() {
        return item.isEmpty();
    }


    public boolean equal(ClipBoardItem target) {
        // type is same?
        var isSameType = (this.isText() && target.isText()) ||
                (this.isImage() && target.isImage());

        // return not same type and type is null.
        if (! isSameType) return false;

        // value is same?
        var isSameValue = false;
        if (this.isText() && target.isText()){
            String me = (String) item.get();
            String param = (String) target.getAsValue();
            isSameValue = me.equals(param);
        }
        return isSameValue;
    }
}

package common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class MyClipBoard {
	//クリップボード
	private static Toolkit kit = Toolkit.getDefaultToolkit();
	private static Clipboard clip = kit.getSystemClipboard();

	/**
	 * @return クリップボードに保存されたテキスト情報
	 */
	public static String getText() {
		try {
			return (String) clip.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			return "";
		}
	}
}

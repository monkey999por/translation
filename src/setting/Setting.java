package setting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Setting {

	private static Properties properties;

	private Setting() {
	};

	/**
	 * @param propPath
	 */
	public static void load(String propPath) {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(Files.newBufferedReader(Paths.get(propPath), StandardCharsets.UTF_8));
			} catch (IOException e) {
				// ファイル読み込みに失敗			
				System.out.println(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", propPath));
			}
		}
	}

	/**
	 * プロパティ値を取得する
	 *
	 * @param key キー
	 * @return 値
	 */
	public static String get(String key) {
		return properties.getProperty(key, null);
	}

}
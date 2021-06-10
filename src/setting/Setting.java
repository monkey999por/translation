package setting;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Singleton design. hold the one {@link Properties} object.
 * require {@link Setting#init(String)} call once.
 * (usually, called by main method. and no call after).
 * properties format is normal ".properties".
 * you can get properties used by {@link Setting#get(String)}
 */
public class Setting {

    private static Properties properties;

    // properties file path.
    private static String filePath;

    private Setting() {
    }

    /**
     * load properties.
     *
     * @param propPath properties file path
     */
    public static void init(String propPath) {
        if (properties == null) {
            filePath = propPath;
            properties = new Properties();
            load(propPath);
        }
    }

    /**
     * if you edit properties when application running , after the call this method .
     * How call? see {@link thread.StandardInObserver} and {@link worker.CommandExcuter}
     */
    public static void reLoad() {
        load(filePath);
    }

    private static void load(String propPath) {
        try {
            properties.load(Files.newBufferedReader(Paths.get(propPath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", propPath));
        }
    }

    /**
     * Get current Properties file path.
     *
     * @return Properties file path
     */
    public static String getFilePath() {
        return filePath;
    }

    /**
     * same as {@link Properties#getProperty(String, String)}.
     *
     * @param key key
     * @return matching key. if there is no matching key, return null.
     */
    private static String get(String key) {
        return properties.getProperty(key, null);
    }

    /**
     * print all properties.
     * used by debug. (or when application start)
     * format
     * 　　■ [key] :  value
     */
    public static void printAll() {
        if (getAsBoolean("debug_mode")) {
            for (var entry : properties.entrySet()) {
                System.out.println("■ [" + entry.getKey() + "] :   " + entry.getValue());
            }
        }
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static String getAsString(String key) {
        return String.valueOf(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static Integer getAsInteger(String key) {
        return Integer.valueOf(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static Double getAsDouble(String key) {
        return Double.valueOf(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static Long getAsLong(String key) {
        return Long.valueOf(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static Boolean getAsBoolean(String key) {
        var b = Boolean.valueOf(get(key));
        if (b == null){
            System.out.println("can't convert to boolean. key is \"" + key + "\"");
            return null;
        } else {
            return b;
        }
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static File getAsFile(String key) {
        return new File(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this.
     * @see Setting#get(String)
     */
    public static URL getAsURL(String key) {
        try {
            return new URL(get(key));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

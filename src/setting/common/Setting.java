package setting.common;

import org.apache.commons.lang3.math.NumberUtils;
import worker.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
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

    // current properties file path.
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
     * How call? see {@link thread.StandardInObserver} and {@link CommandExecutor}
     */
    public static void reLoad() {
        load(filePath);
    }

    private static void load(String propPath) {
        try {
            properties.load(Files.newBufferedReader(Paths.get(propPath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.format("ファイルの読み込みに失敗しました。ファイル名:%s", propPath);
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
     * @return matching key. if there is no matching key, Application Stop.
     */
    private static String get(String key) {
        isBlankKey(key);
        String value = properties.getProperty(key, null);
        if (Objects.isNull(value)) {
            ApplicationFailure("key: " + key + "\r\ngetting value failed.");
            return null;
        } else {
            return value;
        }
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
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static String getAsString(String key) {
        isBlankKey(key);
        return String.valueOf(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static Integer getAsInteger(String key) {
        // check
        isBlankKey(key);
        var value = get(key);
        likeNumber(key, value);

        return Integer.valueOf(value);
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static Double getAsDouble(String key) {
        // check
        isBlankKey(key);
        var value = get(key);
        likeNumber(key, value);

        return Double.valueOf(value);
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static Long getAsLong(String key) {
        // check
        isBlankKey(key);
        var value = get(key);
        likeNumber(key, value);

        return Long.valueOf(value);
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static Boolean getAsBoolean(String key) {
        isBlankKey(key);
        var value = get(key.toLowerCase());
        likeBoolean(key, value);

        return Boolean.valueOf(value);
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static File getAsFile(String key) {
        isBlankKey(key);
        return new File(get(key));
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    public static URL getAsURL(String key) {
        isBlankKey(key);
        try {
            return new URL(get(key));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * checking param is number??
     *
     * @param value checking target.
     */
    private static void likeNumber(String key, String value) {
        isBlankKey(key);
        if (!NumberUtils.isNumber(value)) {
            ApplicationFailure("key : " + key + "\r\nthe property not Number like");
        }
    }

    /**
     * checking param like boolean??
     *
     * @param value checking target.
     */
    private static void likeBoolean(String key, String value) {
        isBlankKey(key);
        if (!(value.equals("true") || value.equals("false"))) {
            ApplicationFailure("key : " + key + "\r\nvalue: " + value + "\r\nspecified \"true\" or \"false\"");
        }
    }

    /**
     * checking key is not null
     *
     * @param key key
     */
    private static void isBlankKey(String key) {
        if (Objects.isNull(key) || key.equals("")) {
            ApplicationFailure("key : " + key + "\r\nproperties get error. key is null");
        }
    }

    /**
     * Application. failed.
     *
     * @param detail Further message
     */
    private static void ApplicationFailure(String detail) {
        System.out.println("application failed. \r\n" +
                "It is possible that the settings are incorrect.\r\n" +
                "properties path: " + filePath);
        System.out.println();
        if (!(Objects.isNull(detail) || detail.equals(""))) {
            System.out.println(detail);
        }
        System.exit(-1);
    }
}


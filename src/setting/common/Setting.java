package setting.common;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
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
    public static synchronized void init(String propPath) {
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

    /**
     * load properties and hold.
     * properties file encoding: utl-80
     *
     * @param propPath properties file path. specified like windows path separator(\)
     */
    private static synchronized void load(String propPath) {
        try {
            properties.load(Files.newBufferedReader(Paths.get(propPath), StandardCharsets.UTF_8));
        } catch (IOException | NullPointerException e) {
            ApplicationFailure(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", propPath));
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
     * used by debug. (or when application start)
     * format
     * 　　■ [key] :  value
     *
     * @return get all properties to String.
     */
    public static String getAllToString() {
        var strings = new StringBuilder();
        for (var entry : properties.entrySet()) {
            strings.append("■ [").append(entry.getKey()).append("] :   ").append(entry.getValue()).append("\r\n");
        }
        return strings.toString();
    }

    /**
     * same as {@link Properties#getProperty(String, String)}.
     *
     * @param key key
     * @return matching key. if there is no matching key, Application Stop.
     */
    @NotNull
    private static String get(String key) {
        isBlankKey(key);
        String value = properties.getProperty(key, null);
        // when value is null, Application exit here.
        if (Objects.isNull(value)) {
            ApplicationFailure("key: " + key + "\r\ngetting value failed.");
        }
        return value.strip();
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    @NotNull
    public static String getAsString(String key) {
        return get(key);
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    @NotNull
    public static Integer getAsInteger(String key) {
        try {
            var value = get(key);
            likeNumber(key, value);
            return Integer.valueOf(value);
        } catch (Exception e) {
            ApplicationFailure("key: " + key + "\r\n\r\n" + e);
            // dummy. unreachable code
            return Integer.MAX_VALUE;
        }
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    @NotNull
    public static Double getAsDouble(String key) {
        try {
            var value = get(key);
            likeNumber(key, value);
            return Double.valueOf(value);
        } catch (Exception e) {
            ApplicationFailure("key: " + key + "\r\n\r\n" + e);
            // dummy. unreachable code
            return Double.MIN_NORMAL;
        }
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    @NotNull
    public static Long getAsLong(String key) {
        try {
            var value = get(key);
            likeNumber(key, value);
            return Long.valueOf(value);
        } catch (Exception e) {
            ApplicationFailure("key: " + key + "\r\n\r\n" + e);
            // dummy. unreachable code
            return Long.MAX_VALUE;
        }
    }

    /**
     * @param key same as {@link Setting#get(String)}
     * @return setting type of convert to this type.
     * @see Setting#get(String)
     */
    @NotNull
    public static Boolean getAsBoolean(String key) {
        try {
            var value = get(key).toLowerCase();
            likeBoolean(key, value);
            return Boolean.valueOf(value);
        } catch (Exception e) {
            ApplicationFailure("key: " + key + "\r\n\r\n" + e);
            // dummy. unreachable code
            return false;
        }
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
     * checking key is not null or blank
     *
     * @param key key
     */
    private static void isBlankKey(String key) {
        if (Objects.isNull(key) || key.equals("")) {
            ApplicationFailure("key : " + key + "\r\nproperties get error. key is empty");
        }
    }

    /**
     * Application. failed.
     *
     * @param detail Further message
     */
    private static void ApplicationFailure(String detail) {
        System.out.println("\r\n" +
                "application failed. \r\n" +
                "It is possible that the settings are incorrect.\r\n" +
                "properties path: " + filePath + "\r\n");
        if (!(Objects.isNull(detail) || detail.equals(""))) {
            System.out.println(detail);
        }
        System.exit(-1);
    }

    /**
     * use test only
     */
    private static void discard() {
        properties = null;
        filePath = null;
    }
}


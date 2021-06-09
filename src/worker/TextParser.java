package worker;

import setting.Setting;

/**
 * common text parser optimize this application.
 */
public class TextParser {

    /**
     * judge is args command. what is command ??
     * - args started in command_prefix?
     *
     * @param str str
     * @return this is command?
     */
    public static boolean isCommand(String str) {
        var chars = str.toCharArray();
        var prefix = Setting.get("command_prefix").toCharArray()[0];
        for (var aChar : chars) {
            if (aChar == ' ') {
                continue;
            } else {
                return aChar == prefix;
            }
        }
        return false;
    }
}

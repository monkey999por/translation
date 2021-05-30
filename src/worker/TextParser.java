package worker;

import setting.Setting;

public class TextParser {
    public static boolean isCommand(String str){
        char[] chars = str.toCharArray();
        char prefix = Setting.get("command_prefix").toCharArray()[0];
        for (char aChar : chars) {
            if (aChar == ' '){
                continue;
            } else {
                return aChar == prefix;
            }
        }
        return false;
    }
}

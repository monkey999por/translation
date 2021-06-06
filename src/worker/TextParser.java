package worker;

import setting.Setting;

public class TextParser {
    public static boolean isCommand(String str){
        var chars = str.toCharArray();
        var prefix = Setting.get("command_prefix").toCharArray()[0];
        for (var aChar : chars) {
            if (aChar == ' '){
                continue;
            } else {
                return aChar == prefix;
            }
        }
        return false;
    }
}

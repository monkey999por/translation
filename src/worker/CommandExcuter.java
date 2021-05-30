package worker;

import common.Cmd;
import setting.Setting;

public class CommandExcuter {
    public static void run(String command){
        try {
            Cmd.execute(false, command
                    .replaceFirst(Setting.get("command_prefix") + "\\s*","")
                    .split(" "));
        } catch (Exception e){
            System.out.println("コマンドの実行に失敗しました: " + command );
        }
    }

    private static boolean isInnerCommand(){
        return false;
    }
}

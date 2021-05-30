package worker;

import common.Cmd;
import setting.Setting;

public class CommandExcuter {
    public static void run(String command){
        try {
            String[] commands = command
                    .replaceFirst(Setting.get("command_prefix") + "\\s*","")
                    .split(" ");
            if (! runInnerCommand(commands[0])){
                Cmd.execute(false, commands);
            }

        } catch (Exception e){
            System.out.println("コマンドの実行に失敗しました: " + command );
        }
    }

    private static boolean runInnerCommand(String command){
        switch (command){
            case "reload":
                Setting.reLoad();
                return true;
            default: return  false;
        }

    }
}

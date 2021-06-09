package worker;

import common.Cmd;
import setting.Setting;

/**
 * any command execute by this.
 */
public class CommandExcuter {

    /**
     * used by input from standard in.
     *
     * @param command format -> default is "{command_prefix} {command}" .
     *                ※ "command_prefix" is defined by setting.properties.
     *                if this is outer command, executed by {@link Cmd#execute(boolean, String[])}.
     *                if this is inner command, executed by {@link CommandExcuter#runInnerCommand(String)}.
     */
    public static void run(String command) {
        try {
            String[] commands = command
                    .replaceFirst(Setting.get("command_prefix") + "\\s*", "")
                    .split(" ");
            if (!runInnerCommand(commands[0])) {
                Cmd.execute(false, commands);
            }

        } catch (Exception e) {
            System.out.println("コマンドの実行に失敗しました: " + command);
        }
    }

    /**
     * run inner command. usually, inner command is method.
     *
     * @param command inner command.
     * @return able to command or not.
     */
    private static boolean runInnerCommand(String command) {
        switch (command) {
            case "reload":
                Setting.reLoad();
                return true;
            default:
                return false;
        }
    }
}

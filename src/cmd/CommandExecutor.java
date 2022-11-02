package cmd;

import app.Debug;
import monkey999.tools.Setting;
import monkey999.tools.Cmd;
import tools.InnerCommands;

import java.lang.reflect.InvocationTargetException;

/**
 * any command execute by this.
 */
public class CommandExecutor {

    /**
     * used by input from standard in.
     *
     * @param command format -> default is "{command_prefix} {command}" .
     *                ※ "command_prefix" is defined by setting.properties.
     *                if this is outer command, executed by {@link Cmd#execute(boolean, String[])}.
     *                if this is inner command, executed by {@link InnerCommands#invoke(String)}.
     */
    public static void run(String command) {
        try {
            String[] commands = command
                    .replaceFirst(Setting.getAsString("command_prefix") + "\\s*", "")
                    .split(" ");
            if (InnerCommands.isInnerCommand(commands[0])) {
                InnerCommands.invoke(commands[0]);
            } else {
                Cmd.execute(false, commands);
            }

        } catch (Exception e) {
            Debug.print(e);
            System.out.println("コマンドの実行に失敗しました: " + command);
        }
    }

    /**
     * judge is args command. what is command ??
     * - args started in command_prefix?
     *
     * @param str str
     * @return this is command?
     */
    public static boolean isCommand(String str) {
        var chars = str.toCharArray();
        var prefix = Setting.getAsString("command_prefix").toCharArray()[0];
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

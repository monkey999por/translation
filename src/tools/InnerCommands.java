package tools;

import app.Debug;
import monkey999.tools.Cmd;
import monkey999.tools.Setting;
import thread.ClipBoardObserver;
import thread.ClipBoardObservers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used by
 */
public class InnerCommands {

    private InnerCommands() {
    }

    ;

    public static void invoke(String command) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = InnerCommands.class;
        var method = InnerCommands.getAll().stream()
                .filter(m -> m.getAnnotation(InnerCommand.class).command().equals(command))
                .findFirst();
        Debug.print("invoke inner command: " + method.get().getAnnotation(InnerCommand.class).command());
        method.get().invoke(null);
    }

    public static boolean isInnerCommand(String command) {
        return !InnerCommands.getAll().stream()
                .filter(m -> m.getAnnotation(InnerCommand.class).command().equals(command))
                .findFirst()
                .isEmpty();
    }

    public static List<InnerCommandBean> getNameAll() {
        var ret = new ArrayList<InnerCommandBean>();
        InnerCommands.getAll().stream()
                .forEach(m ->
                        ret.add(new InnerCommandBean(m.getAnnotation(InnerCommand.class).command(), m.getAnnotation(InnerCommand.class).description())
                        ));
        return ret;
    }

    private static List<Method> getAll() {
        Class<?> clazz = InnerCommands.class;
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getAnnotation(InnerCommand.class) != null)
                .collect(Collectors.toList());
    }

    @InnerCommand(command = "reload", description = "設定ファイルをリロードします。")
    private static void reload() {
        Setting.reLoad();
        System.out.println(Setting.getAllToString());
    }

    @InnerCommand(command = "exit", description = "アプリケーションを終了します。")
    private static void exit() {
        System.exit(0);
    }

    @InnerCommand(command = "setting", description = "設定ファイルを開きます。")
    private static void setting() {
        try {
            Cmd.execute(true, new String[]{"notepad", Setting.getFilePath()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InnerCommand(command = "restart", description = "クリップボードの監視を再開します。")
    private static void restart(){
        ClipBoardObservers.getInstance().start();
    }

    @InnerCommand(command = "stop", description = "クリップボードの監視を停止します。")
    private static void stop(){
        ClipBoardObservers.getInstance().stop();
    }


}


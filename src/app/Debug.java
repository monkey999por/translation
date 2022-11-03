package app;

import monkey999.tools.Setting;

public class Debug {

    private static final String DEBUG_MODE_KEY = "debug_mode";

    /**
     * @return is debug?
     */
    public static boolean debug_mode() {
        return Setting.getAsBoolean(DEBUG_MODE_KEY);
    }

    /**
     * run with debug mode.
     * print to console.
     *
     * @param o message
     */
    public static void print(Object o) {
        if (debug_mode()) {
            System.out.println("new debug");
            if (o instanceof Exception) {
                ((Exception) o).printStackTrace();
            } else {
                System.out.println(o);
            }
        }
    }

    /**
     * run with debug mode.
     * run task.
     *
     * @param task action
     */
    public static void run(Task task) {
        if (debug_mode()) task.run();
    }
}

package app;

public class Debug {

    private static final String DEBUG_MODE_KEY = "debug_mode";

    /**
     * @return is debug?
     */
    public static boolean debug_mode() {
        return Setting.getAsBoolean(DEBUG_MODE_KEY);
    }

    /**
     * run with debug debug mode.
     * same as {@link Exception#printStackTrace()}
     *
     * @param e
     */
    public static void print(Exception e) {
        if (debug_mode()) e.printStackTrace();
    }

    /**
     * run with debug mode.
     * print massage.
     *
     * @param s message
     */
    public static void print(String s) {
        if (debug_mode()) System.out.println(s);
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

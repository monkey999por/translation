package common;

import setting.common.Setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * command prompt wrapper.
 */
public class Cmd {
    /**
     * @param async   - async/await
     * @param command command list. running by {@link ProcessBuilder}.
     * @return running result by command output from standard out.
     */
    public static String execute(boolean async, String[] command) {
        var process = new ProcessBuilder(command);
        Process p;
        process.redirectErrorStream(true);

        try {
            p = process.start();
            if (async) {
                return "";
            }
            p.waitFor();

            var reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            var builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            p.destroy();
            return builder.toString();

        } catch (IOException | InterruptedException e) {

            if (Setting.getAsBoolean("debug_mode")) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

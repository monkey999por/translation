package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import setting.Setting;

public class Cmd {
	/**
	 * @param async
	 * @param command
	 * @return
	 */
	public static String execute(boolean async, String[] command) {
		ProcessBuilder process = new ProcessBuilder(command);
		Process p;
		process.redirectErrorStream(true);

		try {
			p = process.start();
			if (async) {
				return "";
			}
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			p.destroy();
			return builder.toString();

		} catch (IOException | InterruptedException e) {

			if (Boolean.valueOf(Setting.get("debug_mode"))) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

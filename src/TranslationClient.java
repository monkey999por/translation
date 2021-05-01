import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TranslationClient {
	/**
	 * @param command
	 * @return
	 */
	public String runCurl(String[] command) {
		ProcessBuilder process = new ProcessBuilder(command);
		Process p;
		process.redirectErrorStream(true);
		
		try {
			p = process.start();
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

			e.printStackTrace();
		}
		return null;
	}
}

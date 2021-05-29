package common;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import setting.Setting;

import java.util.ArrayList;

public class LangDetector {

	/**
	 * 初期化されているかどうか。直接変更することは不可
	 */
	private static boolean isInit = false;

	private static synchronized void init(String profileDirectory) throws LangDetectException {
		if (!isInit) {
			DetectorFactory.loadProfile(profileDirectory);
			isInit = true;
		}
	}

	/**
	 * @return
	 * @throws LangDetectException
	 */
	public static void init() throws LangDetectException {
		init(Setting.get("lang_detector_profile"));
	}

	public static String detect(String text) throws LangDetectException {
		init();
		Detector detector = DetectorFactory.create();
		detector.append(text);
		return detector.detect();
	}

	public static ArrayList<Language> detectLangs(String text) throws LangDetectException {
		init();
		Detector detector = DetectorFactory.create();
		detector.append(text);
		return detector.getProbabilities();
	}

	/**
	 * @param text
	 * @return 
	 * @throws LangDetectException 
	 */
	public static Boolean isJapanese(String text) throws LangDetectException {
		init();
		switch (detect(text)) {
		case "ja":
		case "ko":
		case "zh-cn":
			return true;
		default:
			return false;
		}

	}
}
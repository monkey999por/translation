package common;

import java.util.ArrayList;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

import setting.Setting;

public class LangDetecter {

	/**
	 * 初期化されているかどうか。直接変更することは不可
	 */
	private static boolean isInit = false;

	private static void init(String profileDirectory) throws LangDetectException {
		if (!isInit) {
			DetectorFactory.loadProfile(profileDirectory);
			isInit = true;
		}
	}

	/**
	 * @return
	 * @throws LangDetectException
	 */
	private static void init() throws LangDetectException {
		init(Setting.get("lang_detecter_profile"));
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
	 * @param detectResult
	 * @return 
	 * @throws LangDetectException 
	 */
	public static Boolean isJapanese(String text) throws LangDetectException {
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
package common;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import setting.Setting;

import java.util.ArrayList;

/**
 * detect language.
 */
public class LangDetector {

    /**
     * is init? changed by {@link LangDetector#init(String)} only.
     */
    private static boolean isInit = false;

    /**
     * init.
     *
     * @param profileDirectory {@link DetectorFactory#loadProfile(String)}
     * @throws LangDetectException
     */
    private static synchronized void init(String profileDirectory) throws LangDetectException {
        if (!isInit) {
            DetectorFactory.loadProfile(profileDirectory);
            isInit = true;
        }
    }

    /**
     * init lang detector.
     *
     * @throws LangDetectException
     */
    private static void init() throws LangDetectException {
        init(Setting.getAsString("lang_detector_profile"));
    }

    /**
     * detect args text language.
     *
     * @param text detect target.
     * @return {@link Detector#detect()}
     * @throws LangDetectException
     */
    public static String detect(String text) throws LangDetectException {
        init();
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.detect();
    }

    /**
     * detect args text language.
     *
     * @param text detect target.
     * @return {@link Detector#getProbabilities()}
     * @throws LangDetectException
     */
    public static ArrayList<Language> detectLang(String text) throws LangDetectException {
        init();
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }

    /**
     * @param text detect args text language.
     * @return is japanese ?
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
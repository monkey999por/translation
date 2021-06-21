package translate;

import app.Setting;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import java.util.ArrayList;

/**
 * detect language.
 */
public class LangDetectorOfCybozuLabs implements LangDetector {

    /**
     * Status of {@link com.cybozu.labs.langdetect}
     */
    private static class Status {
        /**
         * is init? changed by constructor only.
         */
        private static boolean isInit = false;
    }

    /**
     * init.
     *
     * @throws LangDetectException see {@link DetectorFactory#loadProfile(String)}
     */
    public LangDetectorOfCybozuLabs() throws LangDetectException {
        synchronized (this) {
            if (!Status.isInit) {
                DetectorFactory.loadProfile(
                        Setting.getAsString("lang_detector_profile")
                );
                Status.isInit = true;
            }
        }
    }

    /**
     * detect args text language.
     *
     * @param text detect target.
     * @return {@link Detector#detect()}
     * @throws LangDetectException see {@link DetectorFactory#create()}
     */
    @Override
    public TargetLang detect(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        var result = detector.detect();
        switch (result) {
            case "ja":
                return TargetLang.JAPANESE;
            case "ko":
                return TargetLang.KOREA;
            case "zh-cn":
                return TargetLang.CHINESE;
            default:
                return TargetLang.ENGLISH;
        }
    }

    /**
     * detect args text language.
     *
     * @param text detect target.
     * @return {@link Detector#getProbabilities()}
     * @throws LangDetectException see {@link DetectorFactory#create()}
     */
    @Override
    public ArrayList<TargetLang> detects(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        // return detector.getProbabilities();
        return null;
    }

    /**
     * @param text detect args text language.
     * @return is japanese ?
     * @throws LangDetectException see {@link DetectorFactory#create()}
     */
    @Override
    public Boolean isJapanese(String text) throws LangDetectException {
        switch (detect(text)) {
            case JAPANESE:
            case KOREA:
            case CHINESE:
                return true;
            default:
                return false;
        }
    }
}

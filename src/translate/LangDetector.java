package translate;

import com.cybozu.labs.langdetect.Detector;

import java.util.ArrayList;

public interface LangDetector {
    /**
     * detect args text language.
     *
     * @param text detect target.
     */
    public TargetLang detect(String text);

    /**
     * detect args text language.
     *
     * @param text detect target.
     * @return {@link Detector#getProbabilities()}
     */
    public ArrayList<TargetLang> detects(String text);

    /**
     * @param text detect args text language.
     * @return is japanese ?
     */
    public Boolean isJapanese(String text);
}

package tools.language.detector;

public class LangDetectorFactory {
    private LangDetectorFactory() {
    }

    ;

    public static LangDetector newInstance() {
        return new LangDetectorOfCybozuLabs();
    }
}

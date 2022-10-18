package translate;

public class LangDetectorFactory {
    private LangDetectorFactory(){};

    public static LangDetector newInstance(){
        return new LangDetectorOfCybozuLabs();
    }
}

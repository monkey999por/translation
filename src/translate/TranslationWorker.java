package translate;

import app.Setting;
import com.cybozu.labs.langdetect.LangDetectException;
import tools.external.MyTextToSpeechClient;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * worker as a series of processes related to translation.
 */
public class TranslationWorker {

    public TranslationWorker() {

    }

    /**
     * do ...
     * 1. translation text
     * 2. translation text playback!!
     *
     * @param translationText text as translate
     */
    public static void run(String translationText) {
        // translate clipboard text
        var translation = new Callable<String>() {
            @Override
            public String call() throws LangDetectException {
                System.out.println("---------------------------------------------------------");
                System.out.println("■ from -> : " + translationText);
                var result = TranslationClient.translate(translationText);
                // translate result to console
                System.out.println("■ to   -> : " + result);
                System.out.println();
                return result;
            }
        };
        // execute translate clipboard text
        var translateService = Executors.newCachedThreadPool();
        var translationResult = translateService.submit(translation);
        translateService.shutdown();

        /*speech to text run and  playback text to speech result
         * clipboard text is English -> Clipboard text(=English) to speech
         * clipboard text is javanese -> Translation result(=English) to speech
         */
        var textToSpeech = new Runnable() {
            @Override
            public void run() {
                try {
                    // text to speech request
                    MyTextToSpeechClient.request(
                            LangDetector.isJapanese(translationText) ? translationResult.get() : translationText);

                    //  play back text to speech result(mp3)
                    // see -> setting : "google_cloud_text_to_speech_out_audio_file"
                    if (Setting.getAsBoolean("enable_google_cloud_text_to_speech")) {
                        MyTextToSpeechClient.playback();
                    }
                } catch (Exception e) {
                    if (Setting.getAsBoolean("debug_mode")) {
                        e.printStackTrace();
                    }
                }
            }
        };
        // execute text to speech
        var textToSpeechService = Executors.newCachedThreadPool();
        textToSpeechService.submit(textToSpeech);
        textToSpeechService.shutdown();
    }

}

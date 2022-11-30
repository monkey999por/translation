package translate;

import app.Debug;
import monkey999.tools.Setting;
import tools.MyTextToSpeechClient;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * worker as a series of processes related to translation.
 */
public class TranslationWorker {

    // translation tools
    LangDetector detector = LangDetectorFactory.newInstance();

    // thread control init.
    ExecutorService translator = Executors.newCachedThreadPool();
    ExecutorService translateResultOut = Executors.newSingleThreadExecutor();
    ExecutorService textToSpeechService = Executors.newSingleThreadExecutor();

    /**
     * do ...
     * 1. translation text
     * 2. translation text playback!!
     *
     * @param translationText text as translate
     */
    public void run(String translationText) {

        TranslationClient client = TranslationClientFactory.newInstance();

        // request translation
        Future<String> result =
                translator.submit(() -> client.request(translationText));
        // show result
        translateResultOut.submit(() -> {
            while (true) {
                if (!result.isDone()) continue;
                try {
                    System.out.println("---------------------------------------------------------");
                    System.out.println("■ from -> : " + translationText);
                    System.out.println("■ to   -> : " + result.get());

                    if (Setting.getAsBoolean("save_result")) {
                        // TODO: draft
                        Toolkit kit = Toolkit.getDefaultToolkit();
                        Clipboard clip = kit.getSystemClipboard();

                        StringSelection ss = new StringSelection(result.get());
                        clip.setContents(ss, ss);
                    }

                    System.out.println();
                } catch (Exception e) {
                    Debug.print(e);
                } finally {
                    break;
                }
            }
        });

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
                            detector.isJapanese(translationText) ? result.get() : translationText);

                    //  play back text to speech result(mp3)
                    // see -> setting : "google_cloud_text_to_speech_out_audio_file"
                    if (Setting.getAsBoolean("enable_google_cloud_text_to_speech")) {
                        MyTextToSpeechClient.playback();
                    }
                } catch (Exception e) {
                    Debug.print(e);
                }
            }
        };
        // execute text to speech
        textToSpeechService.submit(textToSpeech);
    }
}

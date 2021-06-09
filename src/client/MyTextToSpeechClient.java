package client;

import com.google.cloud.texttospeech.v1.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import setting.Setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * see https://cloud.google.com/text-to-speech
 */
public class MyTextToSpeechClient {
    /**
     * Demonstrates using the Text-to-Speech API.
     * see https://cloud.google.com/text-to-speech/docs/libraries#client-libraries-usage-java
     * see https://github.com/googleapis/java-texttospeech/blob/HEAD/samples/snippets/src/main/java/com/example/texttospeech/QuickstartSample.java
     */
    public static void request(String text) throws Exception {
        if (!Boolean.valueOf(Setting.get("enable_google_cloud_text_to_speech"))) {
            return;
        }
        // Instantiates a client
        try (var textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            var input = SynthesisInput.newBuilder()
                    .setText(text).build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            var voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // Select the type of audio file you want returned
            var audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            var response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            var audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (var out = new FileOutputStream(Setting.get("google_cloud_text_to_speech_out_audio_file"))) {
                out.write(audioContents.toByteArray());
            }
        }
    }

    /**
     * playback text to speech.
     * audio file created {@link MyTextToSpeechClient#request(String)}.
     *
     * @throws JavaLayerException
     * @throws FileNotFoundException
     */
    public static void playback() throws JavaLayerException, FileNotFoundException {
        var audioFile = new FileInputStream(
                new File(Setting.get("google_cloud_text_to_speech_out_audio_file")));
        var audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
        var player = new AdvancedPlayer(audioFile, audioDevice);
        player.play();
    }
}

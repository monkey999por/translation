package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import setting.Setting;

public class MyTextToSpeechClient {
	/** Demonstrates using the Text-to-Speech API. */
	public static void request(String text) throws Exception {
		if (!new Boolean(Setting.get("enable_google_cloud_text_to_speech"))) {
			return;
		}
		// Instantiates a client
		try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
			// Set the text input to be synthesized
			SynthesisInput input = SynthesisInput.newBuilder()
					.setText(text).build();

			// Build the voice request, select the language code ("en-US") and the ssml voice gender
			// ("neutral")
			VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
					.setLanguageCode("en-US")
					.setSsmlGender(SsmlVoiceGender.NEUTRAL)
					.build();

			// Select the type of audio file you want returned
			AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

			// Perform the text-to-speech request on the text input with the selected voice parameters and
			// audio file type
			SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

			// Get the audio contents from the response
			ByteString audioContents = response.getAudioContent();

			// Write the response to the output file.
			try (OutputStream out = new FileOutputStream(Setting.get("google_cloud_text_to_speech_out_audio_file"))) {
				out.write(audioContents.toByteArray());
			}
		}
	}

	/**
	 * playback text to speech
	 * @throws JavaLayerException 
	 * @throws FileNotFoundException 
	 */
	public static void playback() throws JavaLayerException, FileNotFoundException {
		InputStream audioFile = new FileInputStream(
				new File(Setting.get("google_cloud_text_to_speech_out_audio_file")));
		AudioDevice device = FactoryRegistry.systemRegistry().createAudioDevice();
		AdvancedPlayer player = new AdvancedPlayer(audioFile, device);
		player.play();
	}
}

package client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * there is no test. (2021/6/15)
 */
class MyTextToSpeechClientTest {

    /**
     * @see MyTextToSpeechClient#request(String)
     */
    @Test
    @DisplayName("not run playback")
    void request_notRun(){
        assertThat("")
                .isEqualTo("");
    }

    /**
     * @see MyTextToSpeechClient#playback()
     */
    @Test
    @DisplayName("not run playback")
    void playback_notRun(){
        assertThat("")
                .isEqualTo("");
    }
}
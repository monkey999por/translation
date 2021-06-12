package setting.translate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see TargetLanguageCode
 */
class TargetLanguageCodeTest {

    /**
     * @see TargetLanguageCode#toString()
     */
    @Test
    @DisplayName("init JAPANESE and get value")
    void initAndGetCode_ja() {
        assertThat(TargetLanguageCode.JAPANESE.toString())
                .isEqualTo("ja");
    }

    /**
     * @see TargetLanguageCode#toString()
     */
    @Test
    @DisplayName("init ENGLISH and get value")
    void initAndGetCode_en() {
        assertThat(TargetLanguageCode.ENGLISH.toString())
                .isEqualTo("en");
    }
}
package setting.translate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see TargetLanguage
 */
class TargetLanguageTest {

    /**
     * @see TargetLanguage#getLanguageCode()
     */
    @Test
    @DisplayName("init JAPANESE and get value")
    void initAndGetCode_ja() {
        assertThat(TargetLanguage.JAPANESE.getLanguageCode())
                .isEqualTo("ja");
    }

    /**
     * @see TargetLanguage#getLanguageCode()
     */
    @Test
    @DisplayName("init ENGLISH and get value")
    void initAndGetCode_en() {
        assertThat(TargetLanguage.ENGLISH.getLanguageCode())
                .isEqualTo("en");
    }
}
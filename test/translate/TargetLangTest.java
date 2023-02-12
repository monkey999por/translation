package translate;

import constant.TargetLang;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see TargetLang
 */
class TargetLangTest {

    /**
     * @see TargetLang#getLanguageCode()
     */
    @Test
    @DisplayName("init JAPANESE and get value")
    void initAndGetCode_ja() {
        assertThat(TargetLang.JAPANESE.languageCode)
                .isEqualTo("ja");
    }

    /**
     * @see TargetLang#getLanguageCode()
     */
    @Test
    @DisplayName("init ENGLISH and get value")
    void initAndGetCode_en() {
        assertThat(TargetLang.ENGLISH.languageCode)
                .isEqualTo("en");
    }
}

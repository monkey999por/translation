package client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import setting.Setting;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @see <a href="https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html">mockit reference</a>
 */
class TranslationClientTest {

    @InjectMocks
    TranslationClient translationClient;

    /**
     * @see TranslationClient#createRequestUrl(String, String, String)
     */
    @Test
    @DisplayName("test")
    void paramIsJapanese() {

        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("あいうえお", "ソース", "ターゲット"))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A\\\"&source=ソース&target=ターゲット");
        }
    }
}
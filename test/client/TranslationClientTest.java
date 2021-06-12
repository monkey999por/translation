package client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import setting.common.Setting;
import setting.translate.TargetLanguageCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

/**
 * @see <a href="https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html">mockit reference</a>
 */
class TranslationClientTest {

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("all param japanese. the first args encoded.")
    void paramIsJapanese() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("あいうえお", TargetLanguageCode.JAPANESE, TargetLanguageCode.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("all param alphabet")
    void paramIsAlphabet() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("hello", TargetLanguageCode.ENGLISH, TargetLanguageCode.JAPANESE))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"hello\\\"&source=en&target=ja");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("including half-width space before and after, and middle")
    void includingHalfSpace() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl(" hello world ", TargetLanguageCode.JAPANESE, TargetLanguageCode.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"+hello+world+\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("including Full-width space before and after, and middle")
    void includingFullSpace() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("　hello　world　", TargetLanguageCode.JAPANESE, TargetLanguageCode.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%80%80hello%E3%80%80world%E3%80%80\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("including anything(symbol, number, newline code, ..other, like regexp)")
    void mixedAnything() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("12345;^[A-Z]+-*/\\あいう)('&%$#\"!@`*+:/.,。、", TargetLanguageCode.JAPANESE, TargetLanguageCode.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"12345%3B%5E%5BA-Z%5D%2B-*%2F%5C%E3%81%82%E3%81%84%E3%81%86%29%28%27%26%25%24%23%22%21%40%60*%2B%3A%2F.%2C%E3%80%82%E3%80%81\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguageCode, TargetLanguageCode)
     */
    @Test
    @DisplayName("param including null")
    void paramIsNull() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl(null, TargetLanguageCode.JAPANESE, TargetLanguageCode.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"\\\"&source=ja&target=en");
        }
    }
}
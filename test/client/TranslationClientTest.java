package client;

import com.cybozu.labs.langdetect.LangDetectException;
import common.Cmd;
import common.LangDetector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import setting.common.Setting;
import setting.translate.TargetLanguage;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;

/**
 * @see <a href="https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html">mockit reference</a>
 * Exclusion method : {@link TranslationClient#request(String)}
 */
class TranslationClientTest {

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("param japanese. the first args encoded.")
    void createRequestUrl_paramIsJapanese() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("あいうえお", TargetLanguage.JAPANESE, TargetLanguage.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("param alphabet")
    void createRequestUrl_paramIsAlphabet() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("hello", TargetLanguage.ENGLISH, TargetLanguage.JAPANESE))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"hello\\\"&source=en&target=ja");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("including half-width space before and after, and middle")
    void createRequestUrl_includingHalfSpace() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl(" hello world ", TargetLanguage.JAPANESE, TargetLanguage.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"+hello+world+\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("including Full-width space before and after, and middle")
    void createRequestUrl_includingFullSpace() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("　hello　world　", TargetLanguage.JAPANESE, TargetLanguage.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%80%80hello%E3%80%80world%E3%80%80\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("including anything(symbol, number, newline code, like regexp, ..other)")
    void createRequestUrl_mixedAnything() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl("12345;^[A-Z]+-*/\\あいう)('&%$#\"!@`*+:/.,。、", TargetLanguage.JAPANESE, TargetLanguage.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"12345%3B%5E%5BA-Z%5D%2B-*%2F%5C%E3%81%82%E3%81%84%E3%81%86%29%28%27%26%25%24%23%22%21%40%60*%2B%3A%2F.%2C%E3%80%82%E3%80%81\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#createRequestUrl(String, TargetLanguage, TargetLanguage)
     */
    @Test
    @DisplayName("including null")
    void createRequestUrl_paramIsNull() {
        String baseUrl = "https://dummy?text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString())).thenReturn(baseUrl);

            assertThat(TranslationClient.createRequestUrl(null, TargetLanguage.JAPANESE, TargetLanguage.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClient#request(String)
     */
    @Test
    @DisplayName("method \"request\" call subroutine")
    void request_callNext() {
        try (var mockCmd = mockStatic(Cmd.class)) {
            mockCmd.when(()-> Cmd.execute(anyBoolean(), any()))
                    .thenAnswer(invocationOnMock -> {
                        var paramAll = new StringBuffer();
                        paramAll.append(invocationOnMock.getArgument(0).toString()).append(" : ");
                        Arrays.stream((String[])invocationOnMock.getArgument(1))
                                .forEach(o -> {
                                    paramAll.append(o.toString()).append(" : ");
                                });
                        return paramAll.toString();
                    });

            assertThat(TranslationClient.request("request_url"))
                    .isNotNull()
                    .isEqualTo("false : curl : -L : -s : request_url : ");

        }
    }



    /**
     * @see TranslationClient#translate(String)
     */
    @Test
    @DisplayName("convert param and call next inner method")
    void translate_convertParamAndCallNextMethod() throws LangDetectException {
        try (var langDetector = mockStatic(LangDetector.class);
             var client = mockStatic(TranslationClient.class)
        ) {
            langDetector.when(() -> LangDetector.isJapanese(anyString()))
                    .thenReturn(true);
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(1).toString().equals("ja") ? "ja" : "en";
                    });
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.request(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });

            // this is test target. call real method.
            client.when(() -> TranslationClient.translate(anyString()))
                    .thenCallRealMethod();

            assertThat(TranslationClient.translate("ja"))
                    .isEqualTo("ja");
        }
    }

    /**
     * @see TranslationClient#translate(String)
     */
    @Test
    @DisplayName("convert param and call next inner method2")
    void translate_convertParamAndCallNextMethod_2() throws LangDetectException {
        try (var langDetector = mockStatic(LangDetector.class);
             var client = mockStatic(TranslationClient.class)
        ) {
            langDetector.when(() -> LangDetector.isJapanese(anyString()))
                    .thenReturn(false);
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(1).toString().equals("ja") ? "ja" : "en";
                    });
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.request(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });

            // this is test target. call real method.
            client.when(() -> TranslationClient.translate(anyString()))
                    .thenCallRealMethod();

            assertThat(TranslationClient.translate("en"))
                    .isEqualTo("en");
        }
    }

    /**
     * @see TranslationClient#translate(String)
     */
    @Test
    @DisplayName("param is null")
    void translate_paramIsNull() throws LangDetectException {
        try (var langDetector = mockStatic(LangDetector.class);
             var client = mockStatic(TranslationClient.class)
        ) {
            langDetector.when(() -> LangDetector.isJapanese(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0).equals("");
                    });
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0).equals("") ?
                                "first argument is null" : "first argument is not null";
                    });
            // this is mock. called by TranslationClient#translate
            client.when(() -> TranslationClient.request(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });
            // this is test target. call real method.
            client.when(() -> TranslationClient.translate(any()))
                    .thenCallRealMethod();

            assertThat(TranslationClient.translate(null))
                    .isEqualTo("first argument is null");
        }
    }
}
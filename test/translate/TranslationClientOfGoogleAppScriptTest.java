package translate;

import monkey999.tools.Setting;
import com.cybozu.labs.langdetect.LangDetectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import monkey999.tools.Cmd;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @see <a href="https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html">mockit reference</a>
 * Exclusion method : {@link TranslationClientOfGoogleAppScript#translate(String)}
 */
class TranslationClientOfGoogleAppScriptTest {


    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("param japanese. the first args encoded.")
    void createRequestUrl_paramIsJapanese() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            // 呼び出し回数にで戻り値を変更させるやり方。参考：https://reasonable-code.com/mockito-consecutive-calls/
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl("あいうえお", TargetLang.JAPANESE, TargetLang.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("param alphabet")
    void createRequestUrl_paramIsAlphabet() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl("hello", TargetLang.ENGLISH, TargetLang.JAPANESE))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"hello\\\"&source=en&target=ja");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("including half-width space before and after, and middle")
    void createRequestUrl_includingHalfSpace() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl(" hello world ", TargetLang.JAPANESE, TargetLang.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"+hello+world+\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("including Full-width space before and after, and middle")
    void createRequestUrl_includingFullSpace() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl("　hello　world　", TargetLang.JAPANESE, TargetLang.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"%E3%80%80hello%E3%80%80world%E3%80%80\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("including anything(symbol, number, newline code, like regexp, ..other)")
    void createRequestUrl_mixedAnything() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl("12345;^[A-Z]+-*/\\あいう)('&%$#\"!@`*+:/.,。、", TargetLang.JAPANESE, TargetLang.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"12345%3B%5E%5BA-Z%5D%2B-*%2F%5C%E3%81%82%E3%81%84%E3%81%86%29%28%27%26%25%24%23%22%21%40%60*%2B%3A%2F.%2C%E3%80%82%E3%80%81\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#createRequestUrl(String, TargetLang, TargetLang)
     */
    @Test
    @DisplayName("including null")
    void createRequestUrl_paramIsNull() {
        String baseUrl = "https://dummy";
        String param = "text=\\\"{text}\\\"&source={source}&target={target}";
        try (var mockSetting = mockStatic(Setting.class)) {
            mockSetting.when(() -> Setting.getAsBoolean(Mockito.anyString())).thenReturn(true);
            mockSetting.when(() -> Setting.getAsString(Mockito.anyString()))
                    .thenReturn(baseUrl)
                    .thenReturn(param);

            assertThat(TranslationClientOfGoogleAppScript.createRequestUrl(null, TargetLang.JAPANESE, TargetLang.ENGLISH))
                    .isNotNull()
                    .isEqualTo("https://dummy?text=\\\"\\\"&source=ja&target=en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#translate(String)
     */
    @Test
    @DisplayName("method \"request\" call subroutine")
    void callNext() {
        try (var mockCmd = mockStatic(Cmd.class)) {
            mockCmd.when(() -> Cmd.execute(anyBoolean(), any()))
                    .thenAnswer(invocationOnMock -> {
                        var paramAll = new StringBuffer();
                        paramAll.append(invocationOnMock.getArgument(0).toString()).append(" : ");
                        Arrays.stream((String[]) invocationOnMock.getArgument(1))
                                .forEach(o -> {
                                    paramAll.append(o.toString()).append(" : ");
                                });
                        return paramAll.toString();
                    });

            assertThat(TranslationClientOfGoogleAppScript.translate("url"))
                    .isNotNull()
                    .isEqualTo("false : curl : -L : -s : url : ");

        }
    }


    /**
     * @see TranslationClientOfGoogleAppScript#request(String)
     */
    @Test
    @DisplayName("convert param and call next inner method")
    void convertParamAndCallNextMethod() throws LangDetectException {

        LangDetector detector = mock(LangDetectorOfCybozuLabs.class);
        TranslationClientOfGoogleAppScript clientMock = mock(TranslationClientOfGoogleAppScript.class);

        try (
                var client = mockStatic(TranslationClientOfGoogleAppScript.class)
        ) {

            when(detector.isJapanese(anyString()))
                    .thenReturn(true);
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(1).toString().equals("ja") ? "ja" : "en";
                    });
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.translate(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });

            // this is test target. call real method.
            client.when(() -> clientMock.request(anyString()))
                    .thenCallRealMethod();

            assertThat(clientMock.request("ja"))
                    .isEqualTo("ja");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#request(String)
     */
    @Test
    @DisplayName("convert param and call next inner method2")
    void convertParamAndCallNextMethod_2() throws LangDetectException {

        LangDetector detector = mock(LangDetectorOfCybozuLabs.class);
        TranslationClientOfGoogleAppScript clientMock = mock(TranslationClientOfGoogleAppScript.class);

        try (var langDetector = mockStatic(LangDetectorOfCybozuLabs.class);
             var client = mockStatic(TranslationClientOfGoogleAppScript.class)
        ) {
            when(detector.isJapanese(anyString()))
                    .thenReturn(false);
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(1).toString().equals("ja") ? "ja" : "en";
                    });
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.translate(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });

            // this is test target. call real method.
            client.when(() -> clientMock.request(anyString()))
                    .thenCallRealMethod();

            assertThat(clientMock.request("en"))
                    .isEqualTo("en");
        }
    }

    /**
     * @see TranslationClientOfGoogleAppScript#request(String)
     */
    @Test
    @DisplayName("param is null")
    void paramIsNull() throws LangDetectException {
        LangDetector detector = mock(LangDetectorOfCybozuLabs.class);
        TranslationClientOfGoogleAppScript clientMock = mock(TranslationClientOfGoogleAppScript.class);

        try (var langDetector = mockStatic(LangDetectorOfCybozuLabs.class);
             var client = mockStatic(TranslationClientOfGoogleAppScript.class)
        ) {
            when(detector.isJapanese(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0).equals("");
                    });
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.createRequestUrl(anyString(), any(), any()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0).equals("") ?
                                "first argument is null" : "first argument is not null";
                    });
            // this is mock. called by TranslationClientOfGoogleAppScript#translate
            client.when(() -> TranslationClientOfGoogleAppScript.translate(anyString()))
                    .thenAnswer(invocationOnMock -> {
                        return invocationOnMock.getArgument(0);
                    });
            // this is test target. call real method.
            client.when(() -> clientMock.request(anyString()))
                    .thenCallRealMethod();

            assertThat(clientMock.request(null))
                    .isEqualTo("first argument is null");
        }
    }
}

package api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import constant.TargetLang;
import ent.Certification;
import ent.TranslateRequest;
import ent.TranslateResponse;
import monkey999.tools.Setting;
import tools.Debug;
import tools.language.detector.LangDetector;
import tools.language.detector.LangDetectorFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TranslationClientImpl implements TranslationClient {

    private final LangDetector detector = LangDetectorFactory.newInstance();

    @Override
    public String request(String text) {
        try {

            // 言語検出
            Boolean isJapanese = detector.isJapanese(text);
            var sourceLang = isJapanese ? TargetLang.JAPANESE.languageCode : TargetLang.ENGLISH.languageCode;
            var targetLang = isJapanese ? TargetLang.ENGLISH.languageCode : TargetLang.JAPANESE.languageCode;

            HttpClient client = HttpClient.newBuilder().build();

            TranslateRequest requestBody = new TranslateRequest() {{
                setText(text);
                setSource(sourceLang);
                setTarget(targetLang);
                setTranslationClient(Setting.getAsString("translation_client"));
                setCertification(new Certification() {{
                    setApiKey("api_key_dummy");
                }});
            }};
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            Debug.print(requestBodyJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Setting.getAsString("api_server")))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            requestBodyJson,
                            StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                TranslateResponse translateResponse = objectMapper.readValue(response.body(), TranslateResponse.class);
                Debug.print(translateResponse.toString());
                return translateResponse.getText();
            } else {
                Debug.print("Request failed with status code: " + response.statusCode());
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

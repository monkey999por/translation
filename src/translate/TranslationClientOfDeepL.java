package translate;

import app.Debug;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import monkey999.tools.Setting;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslationClientOfDeepL implements TranslationClient {

    private static boolean available = true;
    private static ObjectMapper mapper = new ObjectMapper();


    public TranslationClientOfDeepL() {
        if (Debug.debug_mode()) {
            System.out.println("create instance TranslationClientOfDeepL");
        }
        // 利用上限がきている場合は利用不可
        if (isLimits()) {
            synchronized (TranslationClientOfDeepL.class) {
                TranslationClientOfDeepL.available = false;
            }
        }
    }

    private Boolean isLimits() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Setting.getAsString("deepl.url.check.limit")))
                .version(HttpClient.Version.HTTP_2)
                .header("Authorization", "DeepL-Auth-Key " + Setting.getAsString("deepl.authorization"))
                .header("User-Agent", "translation/1.2.3").build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                HashMap<String, Integer> deeplUsage = mapper.readValue(response.body().toString(),  new TypeReference<Map<String, Integer>>() { });
                if (Debug.debug_mode()){
                    System.out.println("現在の利用文字数: " + deeplUsage.get("character_count"));
                }
                return deeplUsage.get("character_count") > deeplUsage.get("character_limit");
            } else {
                throw new Exception("http status code error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 例外時はその時点でアウト
            return true;
        }
    }

    @Override
    public String request(String text) {
        if (!TranslationClientOfDeepL.available) {
            return "DeepL翻訳は利用上限に達しています。詳細はhttps://www.deepl.com/ja/account/usageでご確認ください。";
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Setting.getAsString("deepl.uri")))
                .version(HttpClient.Version.HTTP_2)
                .header("Authorization", "DeepL-Auth-Key " + Setting.getAsString("deepl.authorization"))
                .header("User-Agent", "translation/1.2.3")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("text=Hello%2C%20world!this&source_lang=EN&target_lang=JA"))
                .build();

        try {
            // リクエストを送信
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // レスポンスボディを出力
            System.out.println(response.body());
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

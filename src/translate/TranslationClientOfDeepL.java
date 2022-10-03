package translate;

import app.Debug;
import monkey999.tools.Setting;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.concurrent.Flow;

public class TranslationClientOfDeepL implements TranslationClient {

    // TODO: synchronizedにしたい
    private static boolean available = true;


    public TranslationClientOfDeepL() {
        if (Debug.debug_mode()) {
            System.out.println("create instance TranslationClientOfDeepL");
        }
        // 利用上限がきている場合は利用不可

    }

    @Override
    public String request(String text) {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Setting.getAsString("deepl.uri")))
                .version(HttpClient.Version.HTTP_2)
                .header("Authorization", Setting.getAsString("deepl.authorization"))
                .header("User-Agent", "translation/1.2.3")
                .header("Content-Type","application/x-www-form-urlencoded")
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

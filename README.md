# translation

## Auto Transration(English <-> Japanese).

翻訳補助アプリです。  
主に以下の操作時に翻訳を行います。
- copy item(*1)
- input text to console.

*1 : target is text, image, or voice. for example, copy text by `ctrl+c`, copy image by `win+shift+s`, or voice
anything.
※Sorry No Suppored copy image and voice Now.

## Build
1. `git clone https://github.com/monkey999por/translation.git`
2. `cd transration`
3. `mvn package`

## 実行前確認
- 事前にgoogle apps scriptで翻訳マクロ作成する
1. access to https://script.google.com/home  
2. 新しいプロジェクトを作成し、以下を貼り付ける。
```javascript
function doGet(e) {
    // リクエストパラメータを取得する
    var p = e.parameter;
    //  LanguageAppクラスを用いて翻訳を実行
    var translatedText = LanguageApp.translate(p.text, p.source, p.target);
    // レスポンスの作成
    var response = ContentService.createTextOutput();
    response.setMimeType(ContentService.MimeType.TEXT);
    // JSONテキストをセットする
    response.setContent(translatedText);

    return response;
}
```  
3. webアプリとしてデプロイする。
4. 表示されたURLをsetting.propertiesに記載する。  
例：translate_request_url=https://script.google.com/macros/s/AKfycbwQQBJ4SFFtZh1kUcsmIbgH9n-2yoLE1YqvfhO7XdQVplEPHytXkHx9Jf-1td_WMxyu/exec?text=\"{text}\"&source={source}&target={target}

## Usage
1. for Windows: `ej.bat`  
   for Linux  : `ej.sh` ※まだ用意していないのでありません。

## 設定
setting.propertiesが`./transration/setting`配下にあります。   
各オプションの説明はプロパティファイル内に記載しています。

## その他の機能
-  翻訳対象、翻訳結果の英語を音声再生することが可能です。以下の設定が必要です。  
   - 1.設定の`enable_google_cloud_text_to_speech`(true/false)で音声再生1の有無を指定。
   - 2.`google_cloud_text_to_speech_out_audio_file`で音声ファイルの一時保存先を適当に指定します。  
   例: C:\\\Users\\\user\\\Downloads\\\out.mp3  
      
  
- 文字化け対策  
  使用するコンソールによっては日本語の文字化けが起こることがあります。  
  その場合、`standard_in_encoding`をコンソールの文字コードに合わせてください。
  
- 入力した文字を翻訳するのではなく、コマンドとして使用することができます。  
  〇利用可能なコマンド
   - `reload`: setting.propertiesをアプリケーションの再起動なく再読み込みします。
   - `exit`: アプリケーションを終了します。
   - その他の外部コマンド。任意のアプリケーションを実行可能です。
   
  ※コマンドを使用するには、プレフィックスの後にコマンドを指定します。  
  例：`:reload`  
  プレフィックスは設定の`command_prefix`で指定します。   

## For developer
- How to debug?  
  intelliJ IDEAを使用します。  
  Run -> Debug.. -> Configuration
  java11を指定。引数に`.\setting\setting.properties`を指定。
  
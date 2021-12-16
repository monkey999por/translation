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
1. `cd transration`
2. `mvn package`

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
    // レスポンスボディの作成
    var body;
    if (translatedText) {
        body = {
          code: 200,
          text: translatedText
        };
    } else {
        body = {
          code: 400,
          text: "Bad Request"
        };
    }
    // レスポンスの作成
    var response = ContentService.createTextOutput();
    // Mime TypeをJSONに設定
    response.setMimeType(ContentService.MimeType.JSON);
    // JSONテキストをセットする
    response.setContent(JSON.stringify(body));

    return response;
}
```  
3. webアプリとしてデプロイする
4. 表示されたURLをsetting.propertiesに記載する

## Usage
1. for Windows: `bin/ej.bat`  
   for Linux  : `bin/ej.sh` ※まだ用意していないのでありません。

## Options
setting.propertiesが`./transration/setting`配下にあります。   
各オプションの説明はプロパティファイル内に記載しています。

# What is

Auto Translation(English <-> Japanese)  
run with...

- copy item(*1)  
   *1 : target is text, image, or voice. for example, copy text by `ctrl+c`, copy image by `win+shift+s`, or voice.

- input text to console.

## Build And Run

```batch
git clone https://github.com/monkey999por/translation.git
cd transration
mvn package
ej.bat
```

## Usage

- you can translate with copy or input.
- run any command.

## About Setting(setting.properties)

- 翻訳対象、翻訳結果の英語を音声再生することが可能です。  
  `enable_google_cloud_text_to_speech`で音声再生の有無を指定。  

  ※使用にはGoogle CloudでText-to-Speechを有効にし、環境変数`GOOGLE_APPLICATION_CREDENTIALS`に生成した秘密鍵のパスを指定してあることが前提です。

- 翻訳エンジンにDeepLを指定することが可能です。

  ```properties
  translation_client=deepl
  ```

- 文字化け対策  
  使用するコンソールによっては日本語の文字化けが起こることがあります。  
  その場合、`standard_in_encoding`をコンソールの文字コードに合わせてください。

- 入力した文字を翻訳するのではなく、コマンドとして使用することができます。  
  
  ※コマンドを使用するには、プレフィックスの後にコマンドを指定します。  
  例：`:reload`  
  プレフィックスは設定の`command_prefix`で指定します。

## For developer

- How to debug?  
  intelliJ IDEAを使用します。  
  Run -> Debug.. -> Configuration
  java17を指定。引数に`.\setting.properties`を指定。
  
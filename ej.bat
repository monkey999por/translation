@echo off
:: %1: "-setting"

:RESTART

if "%1"=="-setting" (
	start "" sakura.exe .\setting\setting.properties
	exit /b 0
)

start "translation" /WAIT /B java -Dfile.encoding="utf-8" -jar .\target\translation-0.0.1-SNAPSHOT-jar-with-dependencies.jar .\setting\setting.properties

::echo "Restart..."
::timeout /T 2
::goto RESTART


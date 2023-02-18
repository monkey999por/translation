@echo off
@pushd %~dp0

:: %1: "-setting"

:RESTART

if "%1"=="-setting" (
	start "" sakura.exe .\setting\setting.properties
	exit /b 0
)

if "%1"=="" goto LATEST
if "%1"=="-v2" goto :VERSION2

:LATEST
start "translation" /WAIT /B java -Dfile.encoding="utf-8" -jar .\target\translation-0.0.1-SNAPSHOT-jar-with-dependencies.jar .\setting.properties
goto END

:VERSION2
start "translation" /WAIT /B java -Dfile.encoding="utf-8" -jar C:\tools\translation\translation-0.0.1-SNAPSHOT_v2.jar C:\tools\translation\setting.properties
pause
GOTO END

::echo "Restart..."
::timeout /T 2
::goto RESTART


:END
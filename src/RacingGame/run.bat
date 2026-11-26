@echo off
setlocal
if not defined JAVA_HOME (
    set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2.1"
)
set "PATH=%PATH%;C:\Users\upadh\tools\apache-maven-3.9.6\bin"

echo ========================================================
echo       DUAL RACER - 2-PLAYER LOCAL TRAFFIC RACER
echo ========================================================
echo Building and running Dual Racer via Maven...
call mvn clean compile exec:java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build or launch failed with exit code %ERRORLEVEL%.
    pause
)
endlocal

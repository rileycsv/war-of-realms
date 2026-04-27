@echo off
title War of Realms Launcher
echo ========================================
echo Building and Running War of Realms...
echo ========================================

echo.
echo [1/5] Creating the bin directory...
if not exist bin mkdir bin

echo.
echo [2/5] Generating source list...
dir /s /B src\*.java > sources.txt

echo.
echo [3/5] Copying asset files into bin directory...
xcopy assets bin\assets /E /I /Y /D > nul

echo.
echo [4/5] Compiling Java files...
:: Use the full path for javac.exe for JDK 25.0.2
"C:\Program Files\Java\jdk-25.0.2\bin\javac.exe" -d bin -cp "src;.\lib\javafx-sdk-25.0.2\lib\*" --module-path ".\lib\javafx-sdk-25.0.2\lib\\" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -sourcepath src @sources.txt

:: Check if the compilation was successful
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [5/5] Starting War of Realms...
"C:\Program Files\Java\jdk-25.0.2\bin\java.exe" --module-path ".\lib\javafx-sdk-25.0.2\lib\\" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -cp bin main.Main

echo.
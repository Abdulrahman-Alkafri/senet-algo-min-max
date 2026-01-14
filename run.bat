@echo off
REM Senet Game Runner Script for Windows

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║          Senet Game - Compilation & Runner             ║
echo ║                    (Windows Version)                   ║
echo ╚════════════════════════════════════════════════════════╝
echo.

REM Compile the Java files
echo [1/2] Compiling Java files...
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo.
    echo ✗ Compilation failed!
    pause
    exit /b 1
)

echo. 
echo ✓ Compilation successful!
echo.

REM Run the game with any command line arguments passed to this script
echo [2/2] Starting Senet Game...
echo.
java -cp bin Main %*

echo.
echo Game finished.
pause
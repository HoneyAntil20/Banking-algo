@echo off
echo Compiling Banking System...
javac *.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Starting Banking Application...
java BankingApp

pause

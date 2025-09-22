@echo off
REM Compile all Java files
javac -cp ".;sqlite-jdbc-3.50.3.0.jar" Main.java database\*.java ui\*.java model\*.java utils\*.java

IF ERRORLEVEL 1 (
    echo Compilation failed.
    pause
    exit /b 1
)

REM Run the program
java -cp ".;sqlite-jdbc-3.50.3.0.jar" Main

pause

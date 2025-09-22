@echo off

REM Compile all Java files into bin folder
javac -d bin -cp ".;sqlite-jdbc-3.50.3.0.jar" Main.java database\*.java ui\*.java model\*.java utils\*.java

IF ERRORLEVEL 1 (
    echo Compilation failed.
    pause
    exit /b 1
)

REM Run the program from bin folder
java -cp "bin;sqlite-jdbc-3.50.3.0.jar" Main

pause

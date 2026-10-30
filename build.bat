@echo off
setlocal

if exist "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe" (
    set "JAVAC=C:\Program Files\Java\jdk-26.0.1\bin\javac.exe"
    set "JAR=C:\Program Files\Java\jdk-26.0.1\bin\jar.exe"
    set "JAVA=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
) else if defined JAVA_HOME (
    set "JAVAC=%JAVA_HOME%\bin\javac.exe"
    set "JAR=%JAVA_HOME%\bin\jar.exe"
    set "JAVA=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVAC=javac"
    set "JAR=jar"
    set "JAVA=java"
)

echo ========================================================
echo   Compiling MiniBank Project and Building JAR
echo ========================================================

if not exist bin mkdir bin
if not exist data mkdir data
if not exist logs mkdir logs

echo Compiling Java source files...
"%JAVAC%" -d bin -sourcepath src src/minibank/MiniBank.java src/minibank/test/MiniBankTest.java src/minibank/test/PracticalDemonstrator.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed.
    exit /b %ERRORLEVEL%
)

echo Packaging runnable minibank.jar...
"%JAR%" cfm minibank.jar manifest.txt -C bin .

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Build completed successfully: minibank.jar created.
) else (
    echo [ERROR] Failed to package JAR.
)

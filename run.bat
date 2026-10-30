@echo off
setlocal

if exist "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" (
    set "JAVA=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
) else if defined JAVA_HOME (
    set "JAVA=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA=java"
)

if not exist minibank.jar (
    echo Building project first...
    call build.bat
)

"%JAVA%" -jar minibank.jar %*

@echo off
echo Creating Shooter.jar
echo.
echo   Compiling files ..
javac *.java
echo   Adding files ..
jar cf Shooter.jar *.class images
echo   Signing archive ..
echo.
jarsigner -keystore "C:\Documents and Settings\All Users\Documents\Java\Keystore" Shooter.jar rui
pause
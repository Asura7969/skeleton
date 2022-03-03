@echo off
set APP_HOME=..
set CLASS_PATH=%APP_HOME%/lib/*;%APP_HOME%/classes;%APP_HOME%/conf;
set SHUTDOWN_CLASS=com.asura.skeleton.Shutdown
set ARGS=http://127.0.0.1:8080/actuator/shutdown
java -classpath %CLASS_PATH% %SHUTDOWN_CLASS% %ARGS%



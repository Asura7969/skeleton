@echo off
set APP_HOME=..
set CLASS_PATH=%APP_HOME%/lib/*;%APP_HOME%/classes;%APP_HOME%/conf;
set APP_MAIN_CLASS=com.asura.skeleton.SkeletonApplication
java -classpath %CLASS_PATH% %APP_MAIN_CLASS%

#!/bin/bash

#Java程序所在的目录（classes的上一级目录）
APP_HOME=..

#需要启动的Java主程序（main方法类）
APP_MAIN_CLASS="com.asura.skeleton.SkeletonApplication"
SHUTDOWN_CLASS="com.asura.skeleton.Shutdown"

#拼凑完整的classpath参数，包括指定lib目录下所有的jar
CLASSPATH="$APP_HOME/conf:$APP_HOME/lib/*:$APP_HOME/classes"

ARGS="http://127.0.0.1:8080/actuator/shutdown"

s_pid=0
checkPid() {
   java_ps=`jps -l | grep $APP_MAIN_CLASS`
   if [ -n "$java_ps" ]; then
      s_pid=`echo $java_ps | awk '{print $1}'`
   else
      s_pid=0
   fi
}

stop() {
checkPid
if [ $s_pid -ne 0 ]; then
    echo -n "Stopping $APP_MAIN_CLASS ...(pid=$s_pid) "
    nohup java -classpath $CLASSPATH $SHUTDOWN_CLASS $ARGS >./shutdown.out 2>&1 &
    if [ $? -eq 0 ]; then
       echo "[OK]"
    else
       echo "[Failed]"
    fi
    sleep 3
    checkPid
    if [ $s_pid -ne 0 ]; then
       stop
    else
       echo "$APP_MAIN_CLASS Stopped"
    fi
else
    echo "================================================================"
    echo "warn: $APP_MAIN_CLASS is not running"
    echo "================================================================"
fi
}

echo "stop project......"
stop

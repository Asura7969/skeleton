#!/bin/bash

#Java程序所在的目录（classes的上一级目录）
APP_HOME=..

APP_MAIN_CLASS="com.asura.skeleton.SkeletonApplication"

CLASSPATH="$APP_HOME/conf:$APP_HOME/lib/*:$APP_HOME/classes"

s_pid=0
checkPid() {
   java_ps=`jps -l | grep $APP_MAIN_CLASS`
   if [ -n "$java_ps" ]; then
      s_pid=`echo $java_ps | awk '{print $1}'`
   else
      s_pid=0
   fi
}

start() {
checkPid
if [ $s_pid -ne 0 ]; then
    echo "================================================================"
    echo "warn: $APP_MAIN_CLASS already started! (pid=$s_pid)"
    echo "================================================================"
else
    echo -n "Starting $APP_MAIN_CLASS ..."
    nohup java -classpath $CLASSPATH $APP_MAIN_CLASS >./st.out 2>&1 &
    checkPid
    if [ $s_pid -ne 0 ]; then
        echo "(pid=$s_pid) [OK]"
    else
        echo "[Failed]"
    fi
fi
}

echo "start project......"
start

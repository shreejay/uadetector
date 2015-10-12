#!/bin/sh 
# Starts, stops, and restarts uadetector.
# 
# chkconfig: 35 92 08 
#   
  
UADETECTOR_DIR="/opt/uadetector/examples/helloworld"

JETTY_RUNNER="target/dependency/jetty-runner.jar" 

WAR_FILE="target/uadetector-hello-world*.war"   

JAVA_OPTIONS="-Xms256m -Xmx256m -DSTOP.PORT=8079   
-Xloggc:/var/log/uadetector_GC.log -XX:+PrintGCTimeStamps -XX:+PrintGCDetails   
-XX:+PrintTenuringDistribution
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log 
-XX:SurvivorRatio=3   
-jar"   
STOP_OPTIONS="--stop-port 8079 --stop-key releasethekraken"
STDERR_LOG_FILE="/var/log/uadetector_stderr.log"
STDOUT_LOG_FILE="/var/log/uadetector_stdout.log"
  
JAVA="/usr/bin/java"  
   
case $1 in
	start)
	echo "Start uadetector" 
	cd $UADETECTOR_DIR  
	$JAVA $JAVA_OPTIONS $JETTY_RUNNER $WAR_FILE $STOP_OPTIONS 1>> $STDOUT_LOG_FILE 2>> $STDERR_LOG_FILE &  
	
	;;
	stop) 
	echo "Stopping Solr"  
	cd $UADETECTOR_DIR  
	$JAVA $JETTY_RUNNER $WAR_FILE $STOP_OPTIONS --stop
	;;
	restart)  
	$0 stop   
	sleep 1   
	$0 start  
	;;
	*)
	echo "Usage: $0 {start|stop|restart}" >&2 
	exit 1
	;;
esac  

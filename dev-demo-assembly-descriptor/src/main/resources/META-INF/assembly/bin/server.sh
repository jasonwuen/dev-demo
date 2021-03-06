#!/bin/bash

# Reference: http://stackoverflow.com/questions/59895/can-a-bash-script-tell-what-directory-its-stored-in
SOURCE="${BASH_SOURCE[0]}"
BIN_DIR="$( dirname "$SOURCE" )"
while [ -h "$SOURCE" ]
do
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
    BIN_DIR="$( cd -P "$( dirname "$SOURCE"  )" && pwd )"
done
BIN_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# Special-case path variables.
case `uname` in
    CYGWIN*)
        BIN_DIR=`cygpath -p -w "$BIN_DIR"`
    ;;
esac

#LIB_DIR=$BIN_DIR/../lib
cd $BIN_DIR/../
LIB_DIR=lib
CONF_DIR=conf
LOGS_DIR=logs

#echo "BIN_DIR=$BIN_DIR"
#echo "LIB_DIR=$LIB_DIR"
#cd $BIN_DIR

# Autodetect JAVA_HOME if not defined
if [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA="$JAVA_HOME/bin/java"
    JPS="$JAVA_HOME/bin/jps"
else
    JAVA=`which java`
    JPS=`which jps`
fi
#echo "JAVA_HOME=$JAVA_HOME"
#echo "JAVA=$JAVA"

if [ ! -x "$JAVA" ]; then
    echo "ERROR: Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME"
    exit 1
fi

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
#JAVA_MEM_OPTS="-server -Xms1024m -Xmx1024m -Xmn256m \
#-Xss256k -XX:+UseConcMarkSweepGC \
#-XX:+UseParNewGC"

CONF_FILE=$CONF_DIR/application.properties
if [ -f "$CONF_FILE" ] ; then
    PROFILES_ACTIVE=`sed '/^spring.profiles.active/!d;s/.*=//' $CONF_FILE`
    LOGS_DIR=`sed '/^logging.path/!d;s/.*=//' $CONF_FILE`
    SERVER_NAME=`sed '/^application.name/!d;s/.*=//' $CONF_FILE`
    JAVA_MEM_OPTS=`sed '/^JAVA_MEM_OPTS/!d;s/.*=//' $CONF_FILE`
fi

#echo "PROFILES_ACTIVE:$PROFILES_ACTIVE"
if [[ "$PROFILES_ACTIVE"x != "default" ]]; then
    ENV_CONF_FILE=$CONF_DIR/application-$PROFILES_ACTIVE.properties
    #echo "ENV_CONF_FILE:$ENV_CONF_FILE"
    if [ -f "$ENV_CONF_FILE" ] ; then
        ENV_JAVA_MEM_OPTS=`sed '/^JAVA_MEM_OPTS/!d;s/.*=//' $ENV_CONF_FILE`
        if [ "$ENV_JAVA_MEM_OPTS"x != ""x ]; then
            JAVA_MEM_OPTS=$ENV_JAVA_MEM_OPTS
        fi
    fi
fi

#echo "INFO:JAVA_MEM_OPTS=$JAVA_MEM_OPTS"
if [ x"$SERVER_NAME" = x ] ; then
    echo "ERROR: Could not find server name with application.properties";
    exit 1
fi

#-Dlog4j.configurationFile=path/to/log4j2.xml
#SERVER_NAME=data-stat
SERVER_PREFIX=$SERVER_NAME

PIDS=$(ps -ef | grep $SERVER_PREFIX | grep -v grep | awk '{print $2}')
if [ -z "$PIDS" ]; then
    PIDS=`$JPS -ml | grep $SERVER_PREFIX | grep -v grep | awk '{print $1}'`
fi

if [ x"$LOGS_DIR" = x ] ; then
    LOGS_DIR=./logs
fi

if [ ! -w "$LOGS_DIR" ] ; then
    mkdir -p "$LOGS_DIR"
fi

_DAEMON_OUT="$LOGS_DIR/$SERVER_NAME.out"

start() {
    if [ -n "$PIDS" ]; then
        echo "ERROR: $SERVER_NAME server process $PIDS already started."
    else
        echo -e "INFO: $SERVER_NAME server starting...\c"

        RUN_JAR=`ls $LIB_DIR | grep .jar | grep -v javadoc | grep -v sources | grep $SERVER_PREFIX`
        #echo "nohup $JAVA $JAVA_OPTS $JAVA_MEM_OPTS -DLOG_PATH=$LOGS_DIR -Dloader.path="$LIB_DIR/,$CONF_DIR/" -jar $LIB_DIR/$RUN_JAR > "$_DAEMON_OUT" 2>&1 < /dev/null &"

        # exec shell
        nohup $JAVA $JAVA_OPTS $JAVA_MEM_OPTS -DLOG_PATH=$LOGS_DIR -Dloader.path="$LIB_DIR/,$CONF_DIR/" -jar $LIB_DIR/$RUN_JAR > "$_DAEMON_OUT" 2>&1 < /dev/null &

        PS_NUM=0
        MAX_RUN_NUM=30
        while [ 1 -eq 1 ]; do
            echo -e ".\c"
            sleep 1
            PIDS=`ps -ef | grep $SERVER_PREFIX | grep -v grep | awk '{print $2}'`
            if [ -z "$PIDS" ]; then
                PIDS=`$JPS -ml | grep $SERVER_PREFIX | grep -v grep | awk '{print $1}'`
            fi
            if [ -n "$PIDS" ]; then
                echo -e "\nINFO: $SERVER_NAME server process $PIDS started success."
                break
            fi
            if [ $PS_NUM -gt $MAX_RUN_NUM ]; then
                echo -e "\nERROR: $SERVER_NAME server started failure, please view the boot log."
                break
            fi
            let PS_NUM++
        done
    fi
}

stop() {
    if [ -z "$PIDS" ]; then
        echo "ERROR: $SERVER_NAME server is not running."
    else
        echo -e "INFO: $SERVER_NAME server process $PIDS stopping...\c"
        kill -s TERM $PIDS
        PS_NUM=0
        MAX_RUN_NUM=30
        while [ 1 -eq 1 ]; do
            echo -e ".\c"
            sleep 1
            PIDS=`ps -ef | grep $SERVER_PREFIX | grep -v grep | awk '{print $2}'`
            if [ -z "$PIDS" ]; then
                PIDS=`$JPS -ml | grep $SERVER_PREFIX | grep -v grep | awk '{print $1}'`
            fi
            if [ -z "$PIDS" ]; then
                echo -e "\nINFO: $SERVER_NAME server stopped success."
                break
            fi
            if [ $PS_NUM -gt $MAX_RUN_NUM ]; then
                kill -9 $PIDS
                echo -e "\nINFO: $SERVER_NAME server process $PIDS forced stopped success."
                break
            fi
            let PS_NUM++
        done
    fi
}

status() {
    if [ -z "$PIDS" ]; then
        echo "ERROR: $SERVER_NAME server is not running."
    else
        echo "INFO: $SERVER_NAME server process $PIDS is running."
    fi
}

restart() {
    stop
    start
}

case $1 in
    start)      start;;
    stop)       stop;;
    status)     status;;
    restart)    restart;;
    *) echo "ERROR: Please input argument $0 {start|stop|restart|status}";;
esac

exit 1
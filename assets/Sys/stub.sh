#!/bin/sh
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
java=java
if test -n "$JAVA_HOME"; then
    java="$JAVA_HOME/bin/java"
fi

java_args="-jar -Xmx1g -XX:+CMSClassUnloadingEnabled"

exec "$java" $java_args $MYSELF "release"
exit 1 

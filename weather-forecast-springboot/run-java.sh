#!/bin/sh

# OpenJdk: enable Generational Shenandoah GC
#export GC_JVM_OPTS="-XX:+AlwaysPreTouch -XX:+UseShenandoahGC -XX:+UnlockExperimentalVMOptions -XX:ShenandoahGCMode=generational -XX:ShenandoahGCHeuristics=adaptive -XX:+UseStringDeduplication -XX:+UseCompressedOops"

# IBM Temurin JDK: OpenJ9 GC and JVM
export GC_JVM_OPTS="-Xgc:concurrentScavenge -XX:+IdleTuningGcOnIdle -XX:IdleTuningMinIdleWaitTime=15"

# -XX:+UnlockCommercialFeatures -XX:-FlightRecorder -XX+DisableAttachMechanism disables the troubleshooting tools like Flight recorder, remote JMX, jcmd, jstack, jmap, and jinfo.
export MISC_JVM_OPTS="-XX:+AlwaysPreTouch -XX:+UseCompressedOops -XX:+UseStringDeduplication -XX+DisableAttachMechanism -XX:+UnlockCommercialFeatures -XX:-FlightRecorder"

export JAVA_OPTS="$GC_JVM_OPTS $MISC_JVM_OPTS"

export JAVA_OPTS="$JAVA_OPTS -server"
export JAVA_OPTS="$JAVA_OPTS --illegal-access=permit"
export JAVA_OPTS="$JAVA_OPTS --illegal-access=warn"
export JAVA_OPTS="$JAVA_OPTS --add-opens java.base/jdk.internal.misc=ALL-UNNAMED"
export JAVA_OPTS="$JAVA_OPTS -Xjit:optlevel=scorching"

echo "JAVA_OPTS=$JAVA_OPTS"
export JAVA_HOME=/opt/jdk/ibm-jdk-21.0.3+9

$JAVA_HOME/bin/java -Xmx256m \
 -jar ./target/weather-forecast-springboot.jar \
 --logging.level.root=INFO \
 --logging.level.webfilter=WARN \
 --logging.level.app=WARN
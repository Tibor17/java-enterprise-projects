#!/bin/sh

# OpenJdk: enable Generational Shenandoah GC
# -XX:+UseShenandoahGC -XX:+UnlockExperimentalVMOptions -XX:ShenandoahGCMode=generational -XX:ShenandoahGCHeuristics=adaptive
export GC_JVM_OPTS="-XX:+UseG1GC -XX:G1HeapRegionSize=1048576 -XX:G1ReservePercent=10 -XX:MaxGCPauseMillis=250"
#
# IBM Temurin JDK: OpenJ9 GC and JVM
# export GC_JVM_OPTS="-Xgc:concurrentScavenge -XX:+IdleTuningGcOnIdle -XX:IdleTuningMinIdleWaitTime=15"

# -XX:+UnlockCommercialFeatures -XX:-FlightRecorder -XX+DisableAttachMechanism disables the troubleshooting tools like Flight recorder, remote JMX, jcmd, jstack, jmap, and jinfo.
export MISC_JVM_OPTS="-XX:+AlwaysPreTouch -XX:+UseCompressedOops -XX:+UseStringDeduplication"

export JAVA_OPTS="$GC_JVM_OPTS $MISC_JVM_OPTS"

export JAVA_OPTS="$JAVA_OPTS -server"
#export JAVA_OPTS="$JAVA_OPTS --illegal-access=permit"
#export JAVA_OPTS="$JAVA_OPTS --illegal-access=warn"
export JAVA_OPTS="$JAVA_OPTS --add-opens java.base/jdk.internal.misc=ALL-UNNAMED"
# for IBM JVM
#export JAVA_OPTS="$JAVA_OPTS -Xjit:optlevel=scorching"
export JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
export JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv6Addresses=false"
export JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.tryReflectionSetAccessible=false"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.noUnsafe=true"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.noPreferDirect=true"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.maxDirectMemory=0"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.allocator.type=unpooled"
export JAVA_OPTS="$JAVA_OPTS -Dio.netty.eventLoopThreads=$(nproc --all)"

export JAVA_OPTS="$JAVA_OPTS -Dquarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/mydatabase"
export JAVA_OPTS="$JAVA_OPTS -Dquarkus.datasource.username=admin"
export JAVA_OPTS="$JAVA_OPTS -Dquarkus.datasource.password=password"

echo "JAVA_OPTS=$JAVA_OPTS"

export JAVA_HOME=/opt/jdk/openjdk-25.0.1

cd target/quarkus-app; $JAVA_HOME/bin/java \
  $JAVA_OPTS \
  -Xmx64m \
  -Xmx128m \
  -jar \
  quarkus-run.jar
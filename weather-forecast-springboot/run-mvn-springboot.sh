#!/bin/sh

export JAVA_HOME=/opt/graalvm/graalvm-ee-jdk21.0.8
export GRAALVM_HOME=/opt/graalvm/graalvm-ee-jdk21.0.8
export M2_HOME=~/Downloads/apache-maven-3.9.11
export PATH=$PATH:$JAVA_HOME/bin
export PATH=$PATH:$M2_HOME/bin

mvn help:describe -Dcmd=spring-boot:run -Ddetail
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dpg.db.username=admin -Dpg.db.password=password -Dspring.profiles.active=dev -Dlogging.level.root=INFO -Dlogging.level.webfilter=WARN -Dlogging.level.app=WARN"
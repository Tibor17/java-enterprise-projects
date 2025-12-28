#!/bin/sh

export JAVA_HOME=/opt/graalvm/graalvm-ee-jdk21.0.8
export GRAALVM_HOME=/opt/graalvm/graalvm-ee-jdk21.0.8
export M2_HOME=/home/tibor17/Downloads/apache-maven-3.9.11
export PATH=$PATH:$JAVA_HOME/bin
export PATH=$PATH:$M2_HOME/bin

mvn -U clean package \
  -Dquarkus.profile=prod \
  -DskipTests \
  -DskipITs
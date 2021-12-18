#!/usr/bin/env bash
export JAVA_PROGRAM_ARGS=`echo "$@"`
mvn compile
mvn exec:java -Dexec.mainClass="org.fractaly.App" -Dexec.args="$JAVA_PROGRAM_ARGS"

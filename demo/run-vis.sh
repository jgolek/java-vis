#!/bin/bash

java -javaagent:$PWD/../agent/build/libs/java-vis-agent-all-0.1.jar="org.jg.demo" -jar build/libs/demo-0.1.jar

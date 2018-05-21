#!/bin/bash

gradle clean jar fatJar

java -javaagent:$PWD/build/libs/java-vis-all-0.1.jar -jar build/libs/java-vis-0.1.jar

#!/bin/bash

apk add openjdk11

./gradlew microBundle

java -jar build/libs/ROOT-microbundle.jar \
--nocluster \
--nohostaware
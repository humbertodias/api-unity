#!/bin/bash

apk add openjdk11

./gradlew microBundle

java -jar dunity-api/build/libs/ROOT-microbundle.jar \
--autoBindSsl \
--nocluster \
--nohostaware

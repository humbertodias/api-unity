#!/bin/bash

apk add openjdk11

git clone https://github.com/humbertodias/api-unity/

cd api-unity

./gradlew microBundle

java -jar build/libs/ROOT-microbundle.jar
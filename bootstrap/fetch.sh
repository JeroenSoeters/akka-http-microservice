#!/bin/bash

ASSETS_FOLDER=./assets
mkdir -p $ASSETS_FOLDER

fetch() {
    curl -v -L -O $1
}

cd $ASSETS_FOLDER

fetch "https://downloads.gradle.org/distributions/gradle-2.11-all.zip"
fetch "https://dl.bintray.com/sbt/native-packages/sbt/0.13.11/sbt-0.13.11.tgz"
fetch "http://downloads.lightbend.com/scala/2.11.7/scala-2.11.7.tgz"
fetch "https://github.com/docker/toolbox/releases/download/v1.10.2/DockerToolbox-1.10.2.pkg"

cd -

echo " ==> Check README.md to fetch JDK DMG"

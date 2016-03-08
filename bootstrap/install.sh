#!/bin/bash

TOOLS_ROOT=${1:-$HOME/xplanning/workspace/PandG_Tools}
MOUNT_POINT=./vol
ASSETS=./assets

install_dmg() {
    if [ ! -f $ASSETS/$1 ]; then
        echo ">>    $2 not found! Please check README"
    else
        hdiutil attach $ASSETS/$1 -mountpoint $MOUNT_POINT
        sudo installer -pkg $MOUNT_POINT/$3 -target /
        hdiutil detach $MOUNT_POINT
    fi
}

install_pkg() {
    if [ ! -f $ASSETS/$1 ]; then
        echo ">>    $2 not found! Please check README"
    else
        sudo installer -pkg $ASSETS/$1 -target /
    fi
}

mkdir -p $TOOLS_ROOT

tar -xzvf $ASSETS/scala*.tgz -C $TOOLS_ROOT
tar -xzvf $ASSETS/sbt*.tgz -C $TOOLS_ROOT

unzip -o $ASSETS/gradle*.zip -d $TOOLS_ROOT

install_dmg "jdk*.dmg" "JDK DMG" "JDK*.pkg"

install_pkg "Docker*.pkg" "Docker PKG"


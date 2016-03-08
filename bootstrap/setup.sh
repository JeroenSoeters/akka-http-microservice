#!/bin/bash

TOOLS_ROOT=${1:-$HOME/xplanning/workspace/PandG_Tools}
PANDG_RC=$HOME/.pandgrc
JDK=/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/

setup_env() {
    echo "Setting up $2"
    echo "export $2=$1" >> $PANDG_RC
    echo "export PATH=\$PATH:\$$2/bin" >> $PANDG_RC
}

find_and_setup_env() {
    tool_root=`find $1 -type d -maxdepth 1 -name $2`
    echo "# Setting up $3" >> $PANDG_RC
    setup_env $tool_root $3
}
rm $PANDG_RC 2> /dev/null
touch $PANDG_RC

find_and_setup_env $TOOLS_ROOT "scala-*" "SCALA_HOME"
find_and_setup_env $TOOLS_ROOT "gradle-*" "GRADLE_HOME"
find_and_setup_env $TOOLS_ROOT "sbt*" "SBT_HOME"
setup_env $JDK "JAVA_HOME" 

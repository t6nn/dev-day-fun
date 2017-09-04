#!/bin/bash

#ls /tmp/codecomp-sessions/ | xargs -I{} ant -f scripts/build.xml -Dprojectdir=/tmp/codecomp-sessions/{}/che-workspace compile

if [[ -z "$1" || ! -d $1 ]]
  then
    echo "First parameter must be a directory."
    exit 1
fi

for dir in `ls -t $1`
do
    PROJECTDIR=$1/${dir}
    echo
    echo "Will compile: $dir"
    cat $PROJECTDIR/game-session.json
    echo
    ant -f build.xml -Dprojectdir=$PROJECTDIR
done
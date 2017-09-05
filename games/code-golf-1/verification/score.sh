#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "src/Solution.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ../score.out
#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/Task3.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
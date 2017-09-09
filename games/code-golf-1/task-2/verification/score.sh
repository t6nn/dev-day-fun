#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/Task2.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
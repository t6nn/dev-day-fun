#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/MissingNumber.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
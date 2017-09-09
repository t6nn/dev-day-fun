#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/ReverseInteger.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
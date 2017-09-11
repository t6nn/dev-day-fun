#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/FindMatrixSum.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
#!/usr/bin/env bash

FILESIZE=$(stat --printf="%s" "../src/FindConnectedBlocks.java")
echo "Final score: $FILESIZE"
echo $FILESIZE > ./score.out
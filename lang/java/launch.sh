#!/bin/bash

./stop.sh

rm -r ./che-workspace

sudo docker run -it --rm \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $PWD/che-data:/data \
  -v $PWD/che-workspace:/chedir \
  eclipse/che dir up

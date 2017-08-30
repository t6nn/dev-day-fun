#!/bin/bash

sudo docker run -it --rm \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $PWD/che-data:/data \
  -v $PWD/che-workspace:/chedir \
  eclipse/che dir down

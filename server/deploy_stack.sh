#!/bin/bash

#echo "admin" | docker secret create jenkins-user -
#echo "admin" | docker secret create jenkins-pass -

docker stack deploy -c jenkins.yml jenkins

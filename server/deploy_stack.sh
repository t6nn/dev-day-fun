#!/bin/bash

#echo "admin" | docker secret create jenkins-user -
#echo "p4ssw0rd" | docker secret create jenkins-pass -

docker stack deploy -c jenkins.yml jenkins && echo "Done. Now install ant (ant-main, latest version) on http://0.0.0.0:8800/configureTools/"

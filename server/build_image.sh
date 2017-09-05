#!/bin/bash

docker image build -t devday/jenkins .
#docker image push devday/jenkins

#docker service create --name jenkins -p 8800:8080 devday/jenkins
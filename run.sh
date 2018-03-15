#!/bin/bash

BASE_DIR="$( dirname "$0" )"

tomcat_port=8080
active_profiles="prod"

if [ "cucumber" = "$1" ]
then
  active_profiles="cucumber"
fi

cd $BASE_DIR
jarname="$(ls -rt ./build/libs/pre-paird-card*.jar | tail -n 1)"
command="java
  -Duser.timezone=UTC \
  -jar $jarname \
  --spring.profiles.active=$active_profiles
  --server.port=$tomcat_port"


echo "Running command: ${command}"
exec ${command}

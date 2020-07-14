#!/usr/bin/env bash
echo 'building is starting...'
cd common-utils/ && mvn clean install deploy
cd ../common-excel/ && mvn clean install deploy
cd ../common-all/ && mvn clean install deploy

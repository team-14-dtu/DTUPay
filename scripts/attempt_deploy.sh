#!/bin/bash
# @author : Petr
set -e

if [[ "$PWD" == *scripts ]]
then
  cd ..
fi

./scripts/run_tests_and_run.sh

COLOR='\033[0;33m'
NC='\033[0m'
printf "${COLOR} --------- Test were a success! Deploying... --------- ${NC} \n"

docker-compose -p dtupay-prod -f docker-compose.prod.yml up -d
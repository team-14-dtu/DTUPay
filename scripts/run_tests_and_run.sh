#!/bin/bash
set -e

COLOR='\033[0;33m'
NC='\033[0m'

if [[ "$PWD" == *scripts ]]
then
  cd ..
fi

./scripts/build_and_run.sh

printf "${COLOR} --------- Starting the tests --------- ${NC}\n"
pushd end-to-end-tests
pushd client-tests
mvn --quiet clean compile # used for generating bank SOAP files
sleep 5 # sleep to wait for stuff to startup
mvn --quiet test




#!/bin/bash
set -e

if [[ "$PWD" == *scripts ]]
then
  cd ..
fi

./scripts/install.sh

# Build all the required images in parallel
COLOR='\033[0;33m'
NC='\033[0m' # No Color
printf "${COLOR} --------- Building microservices in parallel (log is a mess) --------- ${NC} \n"

docker-compose up -d rabbitMQ

pushd microservices
pushd gateway
./build.sh &

popd
pushd payments
./build.sh &

popd
pushd tokens
./build.sh &

popd
pushd accounts
./build.sh &

popd
popd

wait
printf "${COLOR} --------- Done with building --------- ${NC}\n"

# Run the thing
printf "${COLOR} --------- Clearing docker and re-deploying docker images --------- ${NC}\n"
docker image prune -f
docker-compose up -d gateway tokens payments accounts

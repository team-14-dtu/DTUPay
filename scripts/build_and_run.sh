#!/bin/bash
set -e
cd ..

./scripts/install.sh

# Build all the required images in parallel
BLUE='\033[0;34m'
NC='\033[0m' # No Color
printf "${BLUE} --------- Building microservices in parallel (log is a mess) --------- ${NC}"


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
printf "${BLUE} --------- Done with building --------- ${NC}"

# Run the thing
printf "${BLUE} --------- Clearing docker and re-deploying docker images --------- ${NC}"
docker image prune -f
docker-compose up -d rabbitMQ
sleep 10 # wait for rabbitMq to start, otherwise the services could fail
docker-compose up -d gateway tokens payments accounts

cd scripts


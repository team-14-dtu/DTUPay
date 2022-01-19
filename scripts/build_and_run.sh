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

printf "${COLOR} --------- Spinning up rabbitMQ first, so that microservices don't have to wait --------- ${NC} \n"
docker-compose up -d rabbitMQ

printf "${COLOR} --------- Building microservices in parallel (log is a mess) --------- ${NC} \n"
pushd microservices

pushd gateway
./build.sh &
pids[0]=$!

popd
pushd payments
./build.sh &
pids[1]=$!

popd
pushd tokens
./build.sh &
pids[2]=$!

popd
pushd accounts
./build.sh &
pids[3]=$!

popd
popd

# wait for all pids. This way, wait returns the exit status. Otherwise (using just wait), we would ignore failing tests
for pid in ${pids[*]}; do
    wait $pid
done

printf "${COLOR} --------- Done with building --------- ${NC}\n"

# Run the thing
printf "${COLOR} --------- Clearing docker and re-deploying docker images --------- ${NC}\n"
docker image prune -f
docker-compose up -d gateway tokens payments accounts

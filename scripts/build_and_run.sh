#!/bin/bash
set -e
cd ..

# Install latest versions of modules
pushd libs
pushd data
./install.sh

popd
pushd messaging-utilities
./install.sh

popd
popd

# Build all the required images
pushd microservices
pushd gateway
./build.sh

# TODO all others

popd
popd

# Run the thing
docker image prune -f
docker-compose up -d rabbitMQ
sleep 10 # wait for rabbitMq to start, otherwise the services could fail
docker-compose up -d gateway

cd scripts


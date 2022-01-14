#!/bin/bash
set -e

mvn clean install
docker image build . --tag tokens
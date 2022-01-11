#!/bin/bash
set -e

mvn clean package
docker image build . --tag gateway
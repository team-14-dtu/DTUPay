#!/bin/bash
set -e

mvn --quiet clean install
docker image build . --tag tokens
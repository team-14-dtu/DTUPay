#!/bin/bash
set -e

./build_and_run.sh
cd ..

pushd end-to-end-tests
pushd client-tests

mvn test




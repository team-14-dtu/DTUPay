#!/bin/bash
set -e

./build_and_run.sh

pushd end-to-end-tests
pushd client-tests

mvn test



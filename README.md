# Project structure

The project is split into 4 directories.
- end-to-end-tests
  - This folder contains the end-to-end cucumber tests
  **client-tests** and also the API clients.
- libs
  - Code shared between the microservices. 
  - This includes
  slightly modified **messaging-utilities** (to prevent crash)
  with late  starting RabbitMQ
  - And **data**,  which include the events and REST requests
  and responses
- microservices
  - contains the code of all microservices
  which are a separate java projects
- scripts
  - contains scripts that help us with development,
  testing and deployment
  - `install.sh` - installs the libraries
  - `build_and_run.sh` - builds microservice if service tests pass, creates a docker image for 
  each microservice and runs them as docker compose
  app on port **8080**
  - `run_tests_and_run.sh` - runs the end-to-end tests against the compose app
  - `attempt_deploy.sh` - deploys another docker-compose app to port **80**, if the 
    tests pass
  - The scripts are dependent on each other `attempt_deploy.sh` ⸧ `run_tests_and_run.sh` ⸧ `build_and_run.sh` ⸧ `install.sh` 

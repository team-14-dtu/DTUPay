# TODOS
- [ ] Implement microservices
- [ ] Set up unit tests for each microservice
- [ ] Make unit tests run together with end-to-end tests
- [ ] Setup testing in a way that the project does not re-deploy if the tests fail
- [ ] Setup the Dockerfile for the payment paymentService

# How tos:
## Before you start
1. Clone the repository
2. Open the DTUPay folder in Intellij
3. Go through all of the subprojects in `end-to-end-tests`, `libs` and `microservices`,
right click all the pom.xml files and click `Add as Maven project` (it's on the bottom of the
menu)
4. Run the `run_tests.sh` script. This should go through all of the projects and 
install the dependencies


## How to develop:

1. make sure that modules are installed 
   - go to `libs/data` and run `mvn install` or just `install.sh`
     - it is not necessary if you did not change anything and already have them installed
   - go to `libs/messaging-utilities` and run the same commands
     - again, it is only necessary to run it after every change
2. run rabbitMQ docker container
   - there is a script `run_message_queue.sh`
   - you can check whether it worked by going to `http://localhost:15672/`
3. run the microservice
   - either by going to your microservice and running `mvn quarkus:dev` or, in intellij,
   go `Maven` tab on the right, click on your microservice, `plugins -> quarkus -> quarkus:dev`. Then,
   You should find that command in the run configuration in the top right. 
4. Note: Currently, there is only one microservice, but in the future, we will have
more and they will depend on each other. In that case, the **other** microservices will likely
be run using `docker-compose up -d microserviceName`, depending on which you want
to run. The microservice you delep will still be run using `quarkus:dev`

## How to run deployment setup:
1. Inspect the `build_and_run.sh` script. It should install the modules, build images
for microservices and run the `docker-compose.yml`
2. Run the script and check localhost, whether the website is running

## How to test:
1. Just run `run_tests_and_run.sh` script
2. If everything goes well, the deployment setup will be running as well.
This will probably be changed in the future.


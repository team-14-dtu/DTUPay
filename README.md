# How tos:
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
   - either by going to your microservice and running `mvn quarkus:dev` or in intellij,
   go `Maven` tab on the right, click on your microservice, `plugins -> quarkus -> quarkus:dev`. Then,
   You should find that command in the run configuration in the top right. 

## How to run deployment setup:
1. inspect the `build_`

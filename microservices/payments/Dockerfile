FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/payments-1.0-SNAPSHOT.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar payments-1.0-SNAPSHOT.jar prod

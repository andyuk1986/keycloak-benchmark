FROM openjdk:17
EXPOSE 8080
ADD /target/jmxRestClient-0.0.1-SNAPSHOT.jar jmxRestClient.jar
ENTRYPOINT ["java","-jar","jmxRestClient.jar"]
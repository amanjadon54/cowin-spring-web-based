FROM openjdk:8-jdk-alpine
COPY target/cowin-1.0-SNAPSHOT.jar cowin.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/cowin.jar"]
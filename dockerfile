## Builder Image
FROM maven:3.8.3-openjdk-11 AS builder
MAINTAINER Raphael Feitosa
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

## Runner Image
FROM openjdk:11
COPY --from=builder /usr/src/app/target/*.jar /usr/app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
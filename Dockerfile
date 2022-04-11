FROM maven:3.6.3-jdk-8-slim AS build
MAINTAINER Idris
RUN mkdir -p /vkb-api
WORKDIR /vkb-api
COPY pom.xml /vkb-api
COPY flyway.conf /vkb-api
COPY db /vkb-api/db
COPY src /vkb-api/src
RUN mvn flyway:migrate -Dflyway.configFiles=flyway.conf
RUN mvn -B clean package -Dmaven.test.skip=true --file pom.xml

FROM openjdk:8-jdk-alpine
RUN mkdir -p /apps
WORKDIR /apps
COPY --from=build /vkb-api/target/*.jar vkb-api.jar
EXPOSE 9031
ENTRYPOINT ["java","-jar","/apps/vkb-api.jar"]





FROM maven:3.9.4-eclipse-temurin-11-alpine as build
WORKDIR /src
COPY . .
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:latest
WORKDIR /
ENV EPSILON "0.1"
ENV Q 2
ENV MIN_MACHINES 50
ENV MAX_MACHINES 51
ENV ES_HOST "localhost"
ENV ES_INDEX "testdata-"

COPY --from=build --chmod=0777 /src/target/SchedulingAlgorithms-1.0-SNAPSHOT-jar-with-dependencies.jar /app/SchedulingAlgorithms.jar
# use the Epslion ("noops") GC, because we want to measure the time of these algorithms.
ENTRYPOINT ["java", "-jar", "-Xms4G", "-Xmx128G", "-Xss4m", "/app/SchedulingAlgorithms.jar"]
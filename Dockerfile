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
ENV ELASTICSEARCH_HOST "localhost"
ENV ES_INDEX "testdata-"

COPY --from=build /src/target/SchedulingAlgorithms-1.0-SNAPSHOT-jar-with-dependencies.jar SchedulingAlgorithms.jar
# use the Epslion ("noops") GC, because we want to measure the time of these algorithms.
ENTRYPOINT ["java", "-jar", "SchedulingAlgorithms.jar", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseEpsilonGC"]
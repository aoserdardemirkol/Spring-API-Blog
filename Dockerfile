FROM openjdk AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:11
WORKDIR blog
COPY --from=build target/*.jar blog.jar
ENTRYPOINT ["java", "-jar", "blog.jar"]
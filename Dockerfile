# Build stage
FROM maven:3.8.4-openjdk-17 AS build
COPY resumeanalyzer /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]# Build stage

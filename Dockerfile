# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS build

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libtesseract-dev \
    && rm -rf /var/lib/apt/lists/*

COPY resumeanalyzer /app
WORKDIR /app

RUN mvn clean package -DskipTests -e

# Run stage
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache tesseract-ocr

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

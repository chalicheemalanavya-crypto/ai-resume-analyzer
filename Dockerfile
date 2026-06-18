FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY resumeanalyzer /app
WORKDIR /app
# -Dmaven.test.skip=true 
RUN mvn package -DskipTests -Dmaven.compiler.failOnError=false

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache tesseract-ocr
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/terrabia-0.0.1-SNAPSHOT.jar terrabia.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "terrabia.jar"]

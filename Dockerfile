FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar /app/terrabia.jar

ENTRYPOINT ["java", "-jar", "/app/terrabia.jar "]
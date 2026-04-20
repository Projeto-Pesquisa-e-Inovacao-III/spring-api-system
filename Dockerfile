FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/ApiSystem-0.0.1.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080

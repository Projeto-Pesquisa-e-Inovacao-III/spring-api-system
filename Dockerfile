FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/ApiSystem-0.0.1.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/spring_api_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=41465490Fe*

ENV MAIL_HOST=sandbox.smtp.mailtrap.io
ENV MAIL_PORT=2525
ENV MAIL_USERNAME=a
ENV MAIL_PASSWORD=a

ENV PAG_API_URL=http://localhost:8081
ENV INFOBIP_BASE_URL=https://xk4qx3.api.infobip.com
ENV INFOBIP_API_KEY=a
ENV INFOBIP_WHATSAPP_SENDER=a
ENV DISCORD_WEBHOOK_URL=a

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]

EXPOSE 8080:8080
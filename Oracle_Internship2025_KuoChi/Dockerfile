FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/my-dropwizard-app.jar app.jar
COPY config.yml /app/config.yml
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "server", "config.yml"]
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/Oracle_Internship2025_KuoChi-1.0-SNAPSHOT.jar app.jar
COPY config.yml /app/config.yml
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "server", "config.yml"]
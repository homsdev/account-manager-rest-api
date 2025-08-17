FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/account-rest-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-default} -jar app.jar"]
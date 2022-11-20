FROM openjdk:17
ADD target/backend-service.jar backend-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-service.jar"]

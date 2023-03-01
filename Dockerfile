FROM openjdk:17
EXPOSE 8080
ADD target/spring-boot-spots-app.jar spring-boot-spots-app.jar
ENTRYPOINT ["java", "-jar", "/spring-boot-spots-app.jar"]
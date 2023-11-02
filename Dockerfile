#FROM maven:3.8.5-openjdk-17
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:resolve
#COPY . .
#EXPOSE 8089
#
#ENTRYPOINT ["mvn", "spring-boot:run"]

FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
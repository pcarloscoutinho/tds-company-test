FROM maven:3.8.1-jdk-11-slim AS builder

WORKDIR /build
COPY . /build

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:11-jre-slim
COPY --from=builder /build/target/*.jar /app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
# Dockerfile
FROM openjdk:17.0.1-jdk-slim

ARG JAR_FILE=target/*.jar
RUN mkdir -p /opt/crypto
COPY ${JAR_FILE} /opt/crypto/crypto.jar

WORKDIR /opt/crypto

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar crypto.jar"]
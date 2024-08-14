FROM openjdk:21
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} server.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "/server.jar"]
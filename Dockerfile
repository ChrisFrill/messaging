FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} messaging.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/messaging.jar"]
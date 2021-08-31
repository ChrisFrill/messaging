FROM openjdk:11
COPY ./build/libs/messaging-0.0.1.jar messaging.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/messaging.jar"]
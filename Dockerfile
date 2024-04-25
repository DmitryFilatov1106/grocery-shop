FROM openjdk:17-alpine
COPY ./build/libs/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","application.jar"]
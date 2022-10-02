FROM openjdk:18-ea-11-jdk-alpine
VOLUME /tmp
#COPY .aws /root/.aws
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
FROM eclipse-temurin:latest

ADD target/sisbanco-eureka-0.0.1-SNAPSHOT.jar sisbanco-eureka.jar

EXPOSE 8761
 
ENTRYPOINT [ "java", "-jar", "./sisbanco-eureka.jar" ]
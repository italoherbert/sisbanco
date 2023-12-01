FROM eclipse-temurin:latest

ADD target/sisbanco-gateway-0.0.1-SNAPSHOT.jar sisbanco-gateway.jar

EXPOSE 8080
 
ENTRYPOINT [ "java", "-jar", "./sisbanco-gateway.jar" ]
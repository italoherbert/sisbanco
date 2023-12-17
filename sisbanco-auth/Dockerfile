FROM eclipse-temurin:latest

RUN mkdir -p /opt/sisbanco
RUN mkdir -p /opt/sisbanco/conf

WORKDIR /opt/sisbanco

ADD target/sisbanco-auth-0.0.1-SNAPSHOT.jar sisbanco-auth.jar
ADD keycloak-trust.keystore conf/keycloak-trust.keystore

ENTRYPOINT [ "java", "-jar", "./sisbanco-auth.jar" ]
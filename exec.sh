#!/bin/sh

package() {
	cd sisbanco-historico
	./mvnw clean package -DskipTests

	cd ../sisbanco-kernel
	./mvnw clean package -DskipTests

	cd ../sisbanco-keycloak
	./mvnw clean package -DskipTests

	cd ..
}

down() {
	docker-compose down
}

up() {
	docker-compose up --build -d
}

package
down
up

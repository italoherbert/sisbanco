package italo.sisbanco.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SisbancoKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisbancoKeycloakApplication.class, args);
	}

}

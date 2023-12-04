package italo.sisbanco.ext.openfeign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignClientsConfiguration {

	@Bean
	KeycloakMicroserviceClient keycloakMicroservice() {
		return new KeycloakMicroserviceClient();
	}
	
}

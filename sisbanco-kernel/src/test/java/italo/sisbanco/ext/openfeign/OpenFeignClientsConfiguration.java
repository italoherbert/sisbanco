package italo.sisbanco.ext.openfeign;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignClientsConfiguration {

	@Bean
	MockedKeycloakMicroserviceIntegration keycloakMicroserviceIntegration() {
		return mock( MockedKeycloakMicroserviceIntegration.class );
	}
	
}

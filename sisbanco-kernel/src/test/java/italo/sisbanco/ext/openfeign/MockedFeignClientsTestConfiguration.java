package italo.sisbanco.ext.openfeign;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import italo.sisbanco.kernel.integration.KeycloakMicroserviceIntegration;

@TestConfiguration
public class MockedFeignClientsTestConfiguration {

	@Bean
	KeycloakMicroserviceIntegration keycloakMicroserviceIntegration() {
		return mock( MockedKeycloakMicroserviceIntegration.class );
	}
	
}

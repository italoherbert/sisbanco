package italo.sisbanco.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import italo.sisbanco.integration.model.Token;
import italo.sisbanco.integration.model.TokenInfo;

@FeignClient(name="keycloak-microservice", contextId = "keycloak-microservice")
public interface KeycloakMicroserviceIntegration {
	
	@PostMapping("/token-info")
	public ResponseEntity<TokenInfo> tokenInfo( @RequestBody Token token );
	
}

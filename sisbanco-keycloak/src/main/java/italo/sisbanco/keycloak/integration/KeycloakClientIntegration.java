package italo.sisbanco.keycloak.integration;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import italo.sisbanco.keycloak.integration.model.KeycloakToken;

@FeignClient(name="keycloak-integration", contextId = "keycloak")
public interface KeycloakClientIntegration {

	@PostMapping(value="/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseEntity<KeycloakToken> token( Map<String, ?> dados );
	
}

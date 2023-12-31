package italo.sisbanco.kernel.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import italo.sisbanco.kernel.integration.model.Token;
import italo.sisbanco.kernel.integration.model.TokenInfo;
import italo.sisbanco.kernel.integration.model.UserCreated;
import italo.sisbanco.kernel.integration.model.UserSaveRequest;

@Profile("!test")
@FeignClient(name="keycloak-microservice", contextId = "keycloak-microservice")
public interface KeycloakMicroserviceIntegration {
	
	@PostMapping("/token-info")
	public ResponseEntity<TokenInfo> tokenInfo( @RequestBody Token token );
	
	@PostMapping("/users")
	public ResponseEntity<UserCreated> registraUser( 
			@RequestBody UserSaveRequest request, 
			@RequestHeader("Authorization") String authorization ); 
	
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Object> deletaUser( 
			@PathVariable String userId, 
			@RequestHeader("Authorization") String authorization ); 
	
}

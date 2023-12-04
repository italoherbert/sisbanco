package italo.sisbanco.ext.openfeign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import italo.sisbanco.kernel.integration.model.Token;
import italo.sisbanco.kernel.integration.model.TokenInfo;
import italo.sisbanco.kernel.integration.model.UserCreated;
import italo.sisbanco.kernel.integration.model.UserSaveRequest;

public class KeycloakMicroserviceClient {
	
	public ResponseEntity<TokenInfo> tokenInfo( @RequestBody Token token ) {
		return null;
	}
	
	public ResponseEntity<UserCreated> registraUser( 
			UserSaveRequest request, 
			String authorization ) {
		return null;
	}
	
	@DeleteMapping("/users/deleta/{userId}")
	public ResponseEntity<Object> deletaUser( 
			String userId, 
			String authorization ) {
		return null;
	}
	
}

package italo.sisbanco.ext.openfeign;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import italo.sisbanco.kernel.integration.model.Token;
import italo.sisbanco.kernel.integration.model.TokenInfo;
import italo.sisbanco.kernel.integration.model.UserCreated;
import italo.sisbanco.kernel.integration.model.UserSaveRequest;

public class MockedKeycloakMicroserviceIntegration {
	
	public MockedKeycloakMicroserviceIntegration() {
		TokenInfo tokenInfo = mock( TokenInfo.class );
		ResponseEntity<TokenInfo> tokenInfoResp = ResponseEntity.ok( tokenInfo );
		
		UserCreated created = mock( UserCreated.class );
		ResponseEntity<UserCreated> createdResp = ResponseEntity.ok( created );
		
		ResponseEntity<Object> delResp = ResponseEntity.ok().build();
				
		when( registraUser( any( UserSaveRequest.class), anyString() ) ).thenReturn( createdResp );
		when( deletaUser( anyString(), anyString() ) ).thenReturn( delResp );
		when( tokenInfo( any( Token.class ) ) ).thenReturn( tokenInfoResp );	
	}
	
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

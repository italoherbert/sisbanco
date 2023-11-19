package italo.sisbanco.keycloak.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import italo.sisbanco.keycloak.exception.ServiceException;
import italo.sisbanco.keycloak.integration.KeycloakClientIntegration;
import italo.sisbanco.keycloak.integration.model.Token;
import italo.sisbanco.keycloak.message.Erros;
import italo.sisbanco.keycloak.model.request.LoginRequest;
import italo.sisbanco.keycloak.model.response.LoginResponse;

@Service
public class KeycloakService {

	@Value("${keycloak.client_id}")
	private String clientId;
	
	@Value("${keycloak.grant_type}")
	private String grantType;
	
	@Autowired
	private KeycloakClientIntegration keycloakIntegration;
	
	public LoginResponse login( LoginRequest request ) throws ServiceException {
		Map<String, String> dados = new HashMap<>();
		dados.put( "client_id", clientId );
		dados.put( "grant_type", grantType );
		dados.put( "username", request.getUsername() );
		dados.put( "password", request.getPassword() );
		
		try {
			ResponseEntity<Token> tokenClientResp = keycloakIntegration.token( dados );
			if ( tokenClientResp.getStatusCode().is2xxSuccessful() ) {
				Token token = tokenClientResp.getBody();
				
				LoginResponse resp = new LoginResponse();
				resp.setAccessToken( token.getAccess_token() );		
				return resp;
			} else {
				throw new ServiceException( Erros.ACESSO_NAO_AUTORIZADO );
			}
		} catch ( FeignException e ) {
			throw new ServiceException( Erros.ACESSO_NAO_AUTORIZADO );
		}
	}
	
}

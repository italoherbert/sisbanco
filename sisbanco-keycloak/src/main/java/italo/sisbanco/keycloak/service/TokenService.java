package italo.sisbanco.keycloak.service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import italo.sisbanco.keycloak.Erros;
import italo.sisbanco.keycloak.exception.ServiceException;
import italo.sisbanco.keycloak.manager.KeycloakManager;
import italo.sisbanco.keycloak.model.Login;
import italo.sisbanco.keycloak.model.Token;
import italo.sisbanco.keycloak.model.TokenInfo;
import jakarta.ws.rs.WebApplicationException;

@Service
public class TokenService {
	
	@Value("${config.keycloak.app.realm.public_key}")
	private String realmPublicKey;
	
	@Autowired
	private KeycloakManager keycloakManager;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public Token login( Login request ) throws ServiceException {
		try {
			AccessTokenResponse resp = keycloakManager.token( request.getUsername(), request.getPassword() );
			
			Token token = new Token();
			token.setAccessToken( resp.getToken() ); 
			return token;
		} catch ( WebApplicationException e ) {
			e.printStackTrace();
			throw new ServiceException( Erros.TOKEN_SOLICITACAO_FALHA );
		}
		
		/*
		Map<String, String> dados = new HashMap<>();
		dados.put( "client_id", clientId );
		dados.put( "grant_type", grantType );
		dados.put( "username", request.getUsername() );
		dados.put( "password", request.getPassword() );
		
		try {
			ResponseEntity<KeycloakToken> tokenClientResp = keycloakIntegration.token( dados );
			if ( tokenClientResp.getStatusCode().is2xxSuccessful() ) {
				KeycloakToken token = tokenClientResp.getBody();
				
				Token resp = new Token();
				resp.setAccessToken( token.getAccess_token() );		
				return resp;
			} else {
				throw new ServiceException( Erros.ACESSO_NAO_AUTORIZADO );
			}
		} catch ( FeignException e ) {
			throw new ServiceException( Erros.ACESSO_NAO_AUTORIZADO );
		}
		*/
	}
	
	public TokenInfo tokenInfo( Token token ) throws ServiceException {		
		try {
			byte[] publicKeyBytes = Base64.getDecoder().decode( realmPublicKey );
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec( publicKeyBytes );
			
			KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
			PublicKey pubKey = keyFactory.generatePublic( keySpec );
			
			Jws<Claims> claims = Jwts.parser()
				.verifyWith( pubKey )
				.build()
				.parseSignedClaims( token.getAccessToken() );
									
			Claims payload = claims.getPayload();
			String username = payload.get( "preferred_username" ).toString();
			List<String> roles = new ArrayList<>();
											
			
			Map<String, Object> realmAccess = objectMapper.convertValue( payload.get( "realm_access" ), new TypeReference<Map<String, Object>>() {} );
			Map<String, Object> resourceAccess = objectMapper.convertValue( payload.get( "resource_access" ), new TypeReference<Map<String, Object>>() {} );
			Map<String, Object> resourceAccessAccount = objectMapper.convertValue( resourceAccess.get( "account" ), new TypeReference<Map<String, Object>>() {} );
			
			List<Object> realmRoles = objectMapper.convertValue( realmAccess.get( "roles" ), new TypeReference<List<Object>>() {} );
			List<Object> resourceRoles = objectMapper.convertValue(resourceAccessAccount.get( "roles" ), new TypeReference<List<Object>>() {} );
			
			for( Object r : realmRoles )
				roles.add( r.toString() );
			
			for( Object r : resourceRoles )
				roles.add( r.toString() );
			
			TokenInfo info = new TokenInfo();
			info.setUsername( username );
			info.setRoles( roles );
			return info;
		} catch ( MalformedJwtException e ) {
			throw new ServiceException( Erros.TOKEN_INVALIDO );
		} catch ( ExpiredJwtException e ) {
			throw new ServiceException( Erros.TOKEN_EXPIRADO );	
		} catch ( SignatureException e ) {
			throw new ServiceException( Erros.TOKEN_ASSINATURA_INVALIDA );
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new ServiceException( Erros.TOKEN_CHAVE_PUBLICA_INVALIDA );
		}	
	}
	
}

package italo.sisbanco.auth.service;

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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import italo.sisbanco.auth.Erros;
import italo.sisbanco.auth.exception.ErrorException;
import italo.sisbanco.auth.manager.KeycloakManager;
import italo.sisbanco.auth.model.Login;
import italo.sisbanco.auth.model.LoginResponse;
import italo.sisbanco.auth.model.Token;
import italo.sisbanco.auth.model.TokenInfo;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.WebApplicationException;

@Service
public class TokenService {
		
	@Autowired
	private KeycloakManager keycloakManager;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public LoginResponse login( Login request ) throws ErrorException {
		try {
			AccessTokenResponse resp = keycloakManager.token( request.getUsername(), request.getPassword() );

			LoginResponse loginResp = new LoginResponse();
			loginResp.setAccessToken( resp.getToken() ); 
			loginResp.setRefreshToken( resp.getRefreshToken() );
			return loginResp;
		} catch ( NotAuthorizedException e ) {
			throw new ErrorException( Erros.USER_NAO_ENCONTRADO );
		} catch ( BadRequestException e ) {
			e.printStackTrace();
			throw new ErrorException( Erros.TOKEN_SOLICITACAO_FALHA );
		} catch ( WebApplicationException e ) {
			throw new ErrorException( Erros.TOKEN_SOLICITACAO_FALHA );
		}				
	}
			
	public TokenInfo tokenInfo( Token token ) throws ErrorException {
		String realmPublicKey = keycloakManager.getAppRealmPublicKey();
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
			throw new ErrorException( Erros.TOKEN_INVALIDO );
		} catch ( ExpiredJwtException e ) {
			throw new ErrorException( Erros.TOKEN_EXPIRADO );	
		} catch ( SignatureException e ) {
			throw new ErrorException( Erros.TOKEN_ASSINATURA_INVALIDA );
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new ErrorException( Erros.TOKEN_CHAVE_PUBLICA_INVALIDA );
		}	
	}
	
}

package italo.sisbanco.auth.manager;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.Response;

@Component
public class KeycloakManager {

	@Value("${config.keycloak.serverUrl}")
	private String serverUrl;	
		
		
	@Value("${config.keycloak.admin.realm}")
	private String adminRealm;

	@Value("${config.keycloak.admin.client_id}")
	private String adminClientId;
	
	@Value("${config.keycloak.admin.username}")
	private String adminUsername;
	
	@Value("${config.keycloak.admin.password}")
	private String adminPassword;
	
	
	@Value("${config.keycloak.app.realm}")
	private String appRealm;

	@Value("${config.keycloak.app.client_id}")
	private String appClientId;
	
	@Value("${config.keycloak.app.realm.public_key}")
	private String appRealmPublicKey;	
		
	public Keycloak getKeycloakAdmin() {
		return KeycloakBuilder.builder()
				.serverUrl( serverUrl )
				.realm( adminRealm )
				.clientId( adminClientId )
				.username( adminUsername )
				.password( adminPassword )
				.grantType( OAuth2Constants.PASSWORD )
				.build();		
	}
	
	public Keycloak getKeycloakClient( String username, String password ) {
		return KeycloakBuilder.builder()
				.serverUrl( serverUrl )
				.realm( appRealm )
				.clientId( appClientId )				
				.username( username )
				.password( password )
				.grantType( OAuth2Constants.PASSWORD )
				.build();		
	}
	
	public AccessTokenResponse token( String username, String password ) {
		Keycloak keycloak = KeycloakBuilder.builder()
				.serverUrl( serverUrl )
				.realm( appRealm )
				.clientId( appClientId )				
				.username( username )
				.password( password )				
				.grantType( OAuth2Constants.PASSWORD )
				.build();
						
		return keycloak.tokenManager().getAccessToken();
	}		
		
	public Response criaUser( Keycloak keycloak, String realm, UserRepresentation user ) {
		return keycloak.realm( realm ).users().create( user );
	}
	
	public Response deletaUser( Keycloak keycloak, String realm, String userId ) {
		return keycloak.realm( realm ).users().delete( userId );
	}
	
	public UserResource getUserResource( Keycloak keycloak, String realm, String userId ) {
		return keycloak.realm( realm ).users().get( userId );
	}
	
	public GroupRepresentation getGroupRepresentation( Keycloak keycloak, String realm, String groupPath ) {
		return keycloak.realm( realm ).getGroupByPath( groupPath );
	}
		
	public String getAdminRealm() {
		return adminRealm;
	}
	
	public String getAppRealm() {
		return appRealm;
	}
	
	public String getAppRealmPublicKey() {
		return appRealmPublicKey;
	}
	
}

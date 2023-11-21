package italo.sisbanco.keycloak.service;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.keycloak.Erros;
import italo.sisbanco.keycloak.exception.ServiceException;
import italo.sisbanco.keycloak.manager.KeycloakManager;
import italo.sisbanco.keycloak.model.UserCreated;
import italo.sisbanco.keycloak.model.UserSaveRequest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Service
public class UserService {
	
	@Autowired
	private KeycloakManager keycloakManager;
	
	public UserCreated novoUsuario( UserSaveRequest request ) throws ServiceException {
		try {
			Keycloak keycloak = keycloakManager.getKeycloakAdmin();
			String appRealm = keycloakManager.getAppRealm();
			
			UserRepresentation user = new UserRepresentation();
			user.setUsername( request.getUsername() );
			user.setFirstName( request.getFirstName() );
			user.setLastName( request.getLastName() );
			user.setEmail( request.getEmail() );
			user.setEnabled( true ); 
			
			Response resp = keycloakManager.criaUser( keycloak, appRealm, user );
			
			String userId = CreatedResponseUtil.getCreatedId( resp );
			
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setTemporary( false );
			credential.setType( "password" );
			credential.setValue( request.getPassword() );
			
			String groupPath;
			if ( request.getGroup().equalsIgnoreCase( "funcionario" ) ) {
				groupPath = keycloakManager.getFuncionarioGroupPath();
			} else {
				groupPath = keycloakManager.getClienteGroupPath();
			}
						
			GroupRepresentation group = keycloakManager.getGroupRepresentation( keycloak, appRealm, groupPath );
			
			UserResource resource = keycloakManager.getUserResource( keycloak, appRealm, userId );
			resource.resetPassword( credential );			
			resource.joinGroup( group.getId() );			
			
			UserCreated created = new UserCreated();
			created.setUserId( userId );
			return created;
		} catch ( WebApplicationException e ) {
			if ( e.getResponse().getStatus() == 409 )
				throw new ServiceException( Erros.USER_REGISTRO_JA_EXISTE );
			
			e.printStackTrace();
			throw new ServiceException( Erros.USER_REGISTRO_FALHA );
		}
	} 
	
}

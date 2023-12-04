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
import jakarta.ws.rs.NotFoundException;
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
			
			String groupPath = request.getGroupPath();
			
			GroupRepresentation group = null;

			try {
				group = keycloakManager.getGroupRepresentation( keycloak, appRealm, groupPath );			
			} catch ( NotFoundException e ) {
				throw new ServiceException( Erros.USER_PATH_GROUP_NAO_ENCONTRADO );
			}
			
			UserRepresentation user = new UserRepresentation();
			user.setUsername( request.getUsername() );
			user.setFirstName( request.getFirstName() );
			user.setLastName( request.getLastName() );
			user.setEmail( request.getEmail() );
			user.setEnabled( true ); 
			
			Response resp = keycloakManager.criaUser( keycloak, appRealm, user );
			
			if ( resp.getStatus() != 201 )
				throw new ServiceException( Erros.USER_REGISTRO_FALHA );			
			
			String userId = CreatedResponseUtil.getCreatedId( resp );
			
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setTemporary( false );
			credential.setType( "password" );
			credential.setValue( request.getPassword() );								
						
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
	
	public void removeUser( String userId ) throws ServiceException {
		try {
			Keycloak keycloak = keycloakManager.getKeycloakAdmin();
			String appRealm = keycloakManager.getAppRealm();
			
			Response resp = keycloakManager.deletaUser( keycloak, appRealm, userId );
			if ( resp.getStatus() != 200 )
				throw new ServiceException( Erros.USER_DELETE_FALHA );
		} catch ( NotFoundException e ) {
			throw new ServiceException( Erros.USER_NAO_ENCONTRADO );			
		} catch ( WebApplicationException e ) {
			e.printStackTrace();
			throw new ServiceException( Erros.USER_DELETE_FALHA );
		}
	}
	
}

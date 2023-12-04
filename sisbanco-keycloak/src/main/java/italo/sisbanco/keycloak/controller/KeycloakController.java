package italo.sisbanco.keycloak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.keycloak.apidoc.RegistraUserEndpoint;
import italo.sisbanco.keycloak.apidoc.TokenEndpoint;
import italo.sisbanco.keycloak.apidoc.TokenInfoEndpoint;
import italo.sisbanco.keycloak.exception.SistemaException;
import italo.sisbanco.keycloak.model.Login;
import italo.sisbanco.keycloak.model.Token;
import italo.sisbanco.keycloak.model.TokenInfo;
import italo.sisbanco.keycloak.model.UserCreated;
import italo.sisbanco.keycloak.model.UserSaveRequest;
import italo.sisbanco.keycloak.service.TokenService;
import italo.sisbanco.keycloak.service.UserService;

@RestController
@RequestMapping("/api/keycloak")
public class KeycloakController {

	@Autowired
	private TokenService keycloakService;
	
	@Autowired
	private UserService userService;
	
	@TokenEndpoint
	@PostMapping(value="/token")	
	public ResponseEntity<Object> login( @RequestBody Login request ) throws SistemaException {
		Token resp = keycloakService.login( request );
		return ResponseEntity.ok( resp );
	}
	
	@TokenInfoEndpoint
	@PostMapping(value="/token-info")
	public ResponseEntity<Object> tokenInfo( @RequestBody Token token ) throws SistemaException {
		TokenInfo info = keycloakService.tokenInfo( token );
		return ResponseEntity.ok( info );
	}
	
	@RegistraUserEndpoint
	@PreAuthorize("hasAuthority('userCreateWRITE')")
	@PostMapping(value="/users/registra")
	public ResponseEntity<Object> registraUser( @RequestBody UserSaveRequest request ) throws SistemaException {
		UserCreated created = userService.novoUsuario( request );
		return ResponseEntity.ok( created );
	}
	
}

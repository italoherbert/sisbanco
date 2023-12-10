package italo.sisbanco.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.auth.apidoc.DeletaUserEndpoint;
import italo.sisbanco.auth.apidoc.RegistraUserEndpoint;
import italo.sisbanco.auth.apidoc.TokenEndpoint;
import italo.sisbanco.auth.apidoc.TokenInfoEndpoint;
import italo.sisbanco.auth.exception.ErrorException;
import italo.sisbanco.auth.model.Login;
import italo.sisbanco.auth.model.LoginResponse;
import italo.sisbanco.auth.model.Token;
import italo.sisbanco.auth.model.TokenInfo;
import italo.sisbanco.auth.model.UserCreated;
import italo.sisbanco.auth.model.UserSaveRequest;
import italo.sisbanco.auth.service.TokenService;
import italo.sisbanco.auth.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private TokenService keycloakService;
	
	@Autowired
	private UserService userService;
	
	@TokenEndpoint
	@PostMapping("/token")	
	public ResponseEntity<Object> login( @Valid @RequestBody Login request ) throws ErrorException {
		LoginResponse resp = keycloakService.login( request );
		return ResponseEntity.ok( resp );
	}
		
	@TokenInfoEndpoint
	@PostMapping(value="/token-info")
	public ResponseEntity<Object> tokenInfo( @Valid @RequestBody Token token ) throws ErrorException {
		TokenInfo info = keycloakService.tokenInfo( token );
		return ResponseEntity.ok( info );
	}
	
	@RegistraUserEndpoint
	@PreAuthorize("hasAuthority('userCreateWRITE')")
	@PostMapping("/users")
	public ResponseEntity<Object> registraUser( @Valid @RequestBody UserSaveRequest request ) throws ErrorException {
		UserCreated created = userService.novoUsuario( request );
		return ResponseEntity.ok( created );
	}
	
	@DeletaUserEndpoint
	@PreAuthorize("hasAuthority('userDELETE')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Object> deletaUser( String userId ) throws ErrorException {
		userService.removeUser( userId );
		return ResponseEntity.ok().build();
	}
	
}

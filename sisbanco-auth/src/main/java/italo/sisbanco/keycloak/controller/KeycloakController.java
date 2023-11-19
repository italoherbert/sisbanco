package italo.sisbanco.keycloak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.keycloak.model.request.LoginRequest;
import italo.sisbanco.keycloak.model.response.LoginResponse;
import italo.sisbanco.keycloak.service.KeycloakService;
import italo.sisbanco.shared.exception.SistemaException;

@RestController
@RequestMapping("/api/keycloak")
public class KeycloakController {

	@Autowired
	private KeycloakService keycloakService;
	
	@PostMapping(value="/token")
	public ResponseEntity<Object> login( @RequestBody LoginRequest request ) throws SistemaException {
		LoginResponse resp = keycloakService.login( request );
		return ResponseEntity.ok( resp );
	}
	
}

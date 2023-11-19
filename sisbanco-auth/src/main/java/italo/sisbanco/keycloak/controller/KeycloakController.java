package italo.sisbanco.keycloak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.keycloak.service.KeycloakService;
import italo.sisbanco.shared.SistemaException;
import italo.sisbanco.shared.keycloak.TokenRequest;
import italo.sisbanco.shared.keycloak.TokenResponse;

@RestController
@RequestMapping("/api/keycloak")
public class KeycloakController {

	@Autowired
	private KeycloakService keycloakService;
	
	@PostMapping(value="/token")
	public ResponseEntity<Object> login( @RequestBody TokenRequest request ) throws SistemaException {
		TokenResponse resp = keycloakService.login( request );
		return ResponseEntity.ok( resp );
	}
	
}

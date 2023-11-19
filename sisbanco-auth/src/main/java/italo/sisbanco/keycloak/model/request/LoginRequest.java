package italo.sisbanco.keycloak.model.request;

import lombok.Data;

@Data
public class LoginRequest {

	private String username;
	
	private String password;
	
}

package italo.sisbanco.keycloak.integration.model;

import lombok.Data;

@Data
public class KeycloakToken {

	private String access_token;
	
	/*
	private int expiresIn;
	
	private int refreshExpiresIn;
	
	private String refreshToken;
	
	private String tokenType;
	
	private int notBeforePolicy;
	
	private String sessionState;	
	
	private String scope;
	*/
	
}

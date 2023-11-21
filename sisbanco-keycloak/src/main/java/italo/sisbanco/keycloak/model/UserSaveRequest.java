package italo.sisbanco.keycloak.model;

import lombok.Data;

@Data
public class UserSaveRequest {

	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String group;
	
}

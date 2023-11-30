package italo.sisbanco.kernel.integration.model;

import lombok.Data;

@Data
public class UserSaveRequest {

	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String groupPath;
	
}

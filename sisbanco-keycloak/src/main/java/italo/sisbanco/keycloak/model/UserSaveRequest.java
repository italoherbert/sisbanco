package italo.sisbanco.keycloak.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSaveRequest {

	@NotBlank(message = "{user.registro.username.obrigatorio}")
	private String username;
	
	@NotBlank(message = "{user.registro.password.obrigatorio}")
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	@NotBlank(message = "{user.registro.groupPath.obrigatorio}")
	private String groupPath;
	
}

package italo.sisbanco.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Login {

	@NotBlank(message = "{login.username.obrigatorio}")
	private String username;
	
	@NotBlank(message = "{login.password.obrigatorio}")	
	private String password;
	
}

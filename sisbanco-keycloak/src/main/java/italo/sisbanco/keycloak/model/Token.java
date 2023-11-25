package italo.sisbanco.keycloak.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Token {

	@NotBlank(message = "{token.obrigatorio}") 
	private String accessToken;	
	
}

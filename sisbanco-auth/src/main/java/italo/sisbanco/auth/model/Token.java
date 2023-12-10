package italo.sisbanco.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Token {

	@NotBlank(message = "{token.obrigatorio}") 
	private String accessToken;	
		
}

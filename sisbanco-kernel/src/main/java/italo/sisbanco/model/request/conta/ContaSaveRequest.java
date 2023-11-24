package italo.sisbanco.model.request.conta;

import italo.sisbanco.integration.model.UserSaveRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ContaSaveRequest {

	@NotEmpty(message = "{titular.obrigatorio}")
	private String titular;
	
	private UserSaveRequest user;
		
}

package italo.sisbanco.kernel.model.request.conta;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ContaFiltroRequest {

	@NotEmpty(message = "{titular.obrigatorio}")
	private String titular;
	
}

package italo.sisbanco.model.request.conta;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ValorRequest {
	
	@DecimalMin(value="0.00000000001", message = "{valor.negativo.ou.zero}")
	private double valor;
	
}

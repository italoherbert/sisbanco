package italo.sisbanco.model.response.conta;

import lombok.Data;

@Data
public class ContaResponse {

	private Long id;
	
	private String titular;
	
	private double saldo;
	
	private double credito;
	
}

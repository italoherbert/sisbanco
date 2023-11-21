package italo.sisbanco.model.response.conta;

import lombok.Data;

@Data
public class ContaResponse {

	private String titular;
	
	private double saldo;
	
	private double credito;
	
}

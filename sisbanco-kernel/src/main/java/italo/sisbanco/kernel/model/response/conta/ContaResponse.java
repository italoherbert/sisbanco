package italo.sisbanco.kernel.model.response.conta;

import lombok.Data;

@Data
public class ContaResponse {

	private Long id;
	
	private String titular;
	
	private String username;
	
	private double saldo;
	
	private double credito;
	
	private double limiteOperacao;
		
}

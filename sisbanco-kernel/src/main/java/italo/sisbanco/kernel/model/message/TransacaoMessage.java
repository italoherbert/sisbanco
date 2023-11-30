package italo.sisbanco.kernel.model.message;

import java.util.Date;

import lombok.Data;

@Data
public class TransacaoMessage {

	private String username;
	
	private double valor;
		
	private Date dataOperacao;
	
	private String tipo;
	
}

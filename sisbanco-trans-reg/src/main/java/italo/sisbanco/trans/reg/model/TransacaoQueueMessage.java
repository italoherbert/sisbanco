package italo.sisbanco.trans.reg.model;

import java.util.Date;

import lombok.Data;

@Data
public class TransacaoQueueMessage {

	private String username;
	
	private double valor;
		
	private Date dataOperacao;
	
	private String tipo;
	
}

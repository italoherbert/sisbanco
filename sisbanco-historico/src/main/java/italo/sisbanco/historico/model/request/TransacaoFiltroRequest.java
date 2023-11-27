package italo.sisbanco.historico.model.request;

import java.util.Date;

import lombok.Data;

@Data
public class TransacaoFiltroRequest {

	private String username;
		
	private Date dataInicio;
	
	private Date dataFim;
	
}

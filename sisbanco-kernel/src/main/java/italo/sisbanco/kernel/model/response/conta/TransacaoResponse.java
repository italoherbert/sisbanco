package italo.sisbanco.kernel.model.response.conta;

import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import lombok.Data;

@Data
public class TransacaoResponse {
	
	private double saldoAnterior;
	
	private double saldoAtual;
	
	private boolean realizada;
	
	private TransacaoTipo tipo;
	
}

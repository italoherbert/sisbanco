package italo.sisbanco.kernel.model.response.conta;

import italo.sisbanco.kernel.model.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.enums.ValorEmContaTipo;
import lombok.Data;

@Data
public class OperacaoPendenteResponse {
	
	private boolean realizada;

	private double saldoAnterior;
	
	private double saldoAtual;
	
	private ContaResponse conta;
		
	private OperacaoPendenteTipo operacaoTipo;
	
	private ValorEmContaTipo valorEmContaTipo;
	
	private TransacaoTipo transacaoTipo;
	
}

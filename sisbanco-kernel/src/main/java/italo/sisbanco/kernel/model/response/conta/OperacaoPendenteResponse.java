package italo.sisbanco.kernel.model.response.conta;

import java.util.Date;

import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import lombok.Data;

@Data
public class OperacaoPendenteResponse {
		
	private String operId;

	private OperacaoPendenteStatus status;
	
	private double saldoAnterior;
	
	private double valor;
		
	private ContaResponse conta;
	
	public Date dataCriacao;
	
	public Date dataOperacao;
		
	private OperacaoPendenteTipo operacaoTipo;
	
	private AlteraValorEmContaTipo alteraValorEmContaTipo;
	
	private TransacaoTipo transacaoTipo;
	
}

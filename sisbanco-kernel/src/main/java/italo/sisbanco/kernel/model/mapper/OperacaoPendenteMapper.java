package italo.sisbanco.kernel.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;

@Component
public class OperacaoPendenteMapper {

	@Autowired
	private ContaMapper contaMapper;	
	
	public OperacaoPendenteResponse alterValorEmContaResponse( Conta conta, AlteraValorEmContaTipo tipo ) {				
		double saldo = conta.getSaldo();
		
		return OperacaoPendenteResponseBuilder.builder()
				.conta( conta, contaMapper )
				.saldoAnterior( saldo )
				.alterValorEmContaTipo( tipo )
				.realizada( false )				
				.get();				
	}
	
	public OperacaoPendenteResponse transacaoResponse( Conta conta, TransacaoTipo tipo ) {
		double saldo = conta.getSaldo();
		
		return OperacaoPendenteResponseBuilder.builder()
				.conta( conta, contaMapper )
				.saldoAnterior( saldo )
				.transacaoTipo( tipo )
				.realizada( false ) 
				.get();		
	}
	
}

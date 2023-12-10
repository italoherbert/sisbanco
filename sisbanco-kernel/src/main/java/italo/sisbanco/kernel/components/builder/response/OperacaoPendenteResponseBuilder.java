package italo.sisbanco.kernel.components.builder.response;

import java.util.Date;

import italo.sisbanco.kernel.components.mapper.ContaMapper;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.OperacaoPendenteCache;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;

public class OperacaoPendenteResponseBuilder {

	private OperacaoPendenteResponse resp;
	
	public OperacaoPendenteResponseBuilder() {		
		resp = new OperacaoPendenteResponse();
		resp.setDataOperacao( new Date() );
	}
	
	public static OperacaoPendenteResponseBuilder builder() {
		return new OperacaoPendenteResponseBuilder();
	}
	
	public OperacaoPendenteResponse get() {
		return resp;
	}
		
	public OperacaoPendenteResponseBuilder status( OperacaoPendenteStatus status ) {
		resp.setStatus( status );
		return this;
	}
	
	public OperacaoPendenteResponseBuilder operacaoPendente( OperacaoPendenteCache oper ) {
		if ( oper != null )
			resp.setOperId( oper.getId() );
		return this;
	}
	
	public OperacaoPendenteResponseBuilder conta( Conta conta, ContaMapper contaMapper ) {
		ContaResponse contaResp = contaMapper.novoContaResponse();
		contaMapper.carregaResponse( contaResp, conta );		
		resp.setConta( contaResp );
		return this;
	}
	
	public OperacaoPendenteResponseBuilder transacaoTipo( TransacaoTipo tipo ) {
		resp.setOperacaoTipo( OperacaoPendenteTipo.TRANSACAO );
		resp.setTransacaoTipo( tipo );
		return this;
	}
	
	public OperacaoPendenteResponseBuilder alterValorEmContaTipo( AlteraValorEmContaTipo tipo ) {
		resp.setOperacaoTipo( OperacaoPendenteTipo.ALTER_VALOR_EM_CONTA );
		resp.setAlteraValorEmContaTipo( tipo ); 
		return this;
	}
		
	public OperacaoPendenteResponseBuilder dataCriacao( Date dataCriacao ) {
		resp.setDataCriacao( dataCriacao );
		return this;
	}
	
	public OperacaoPendenteResponseBuilder valor( double valor ) {
		resp.setValor( valor );
		return this;
	}
		
	public OperacaoPendenteResponseBuilder saldoAnterior( double valor ) {
		resp.setSaldoAnterior( valor );
		return this;
	}
		
}

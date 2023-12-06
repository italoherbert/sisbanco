package italo.sisbanco.kernel.components.builder;

import java.util.Date;

import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.OperacaoPendenteCache;

public class AlteraValorEmContaCacheBuilder {
	
	private AlteraValorEmContaCache alteraValorEmConta;
	
	public AlteraValorEmContaCacheBuilder() {
		OperacaoPendenteCache oper = new OperacaoPendenteCache();
		oper.setTipo( OperacaoPendenteTipo.ALTER_VALOR_EM_CONTA );
		
		alteraValorEmConta = new AlteraValorEmContaCache();
		alteraValorEmConta.setOperacaoPendente( oper ); 
	}
	
	public static AlteraValorEmContaCacheBuilder builder() {
		return new AlteraValorEmContaCacheBuilder();
	}
	
	public AlteraValorEmContaCache get() {
		return alteraValorEmConta;
	}
	
	public AlteraValorEmContaCacheBuilder id( String id ) {
		alteraValorEmConta.setId( id );
		return this;
	}
	
	public AlteraValorEmContaCacheBuilder contaId( long contaOrigemId ) {
		alteraValorEmConta.setContaId( contaOrigemId );
		return this;
	}
		
	public AlteraValorEmContaCacheBuilder valor( double valor ) {
		alteraValorEmConta.setValor( valor );
		return this;
	}
	
	public AlteraValorEmContaCacheBuilder dataOperacao( Date dataOp ) {
		alteraValorEmConta.setDataOperacao( dataOp );
		return this;
	}
	
	public AlteraValorEmContaCacheBuilder tipo( AlteraValorEmContaTipo tipo ) {
		alteraValorEmConta.setTipo( tipo );
		return this;
	}
	
	public AlteraValorEmContaCacheBuilder operacaoPendenteTipo( OperacaoPendenteTipo tipo ) {
		alteraValorEmConta.getOperacaoPendente().setTipo( tipo );
		return this;
	}			
	
}


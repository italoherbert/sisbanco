package italo.sisbanco.kernel.components.builder;

import java.util.Date;
import java.util.UUID;

import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.cache.OperacaoPendenteCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;

public class TransacaoCacheBuilder {
		
	private TransacaoCache transacao;
	
	public TransacaoCacheBuilder() {
		OperacaoPendenteCache oper = new OperacaoPendenteCache();
		oper.setId( UUID.randomUUID().toString() ); 
		oper.setTipo( OperacaoPendenteTipo.TRANSACAO );
		
		transacao = new TransacaoCache();
		transacao.setId( UUID.randomUUID().toString() ); 
		transacao.setOperacaoPendente( oper );
		transacao.setDataCriacao( new Date() ); 
	}
	
	public static TransacaoCacheBuilder builder() {
		return new TransacaoCacheBuilder();
	}
	
	public TransacaoCache get() {
		return transacao;
	}
	
	public TransacaoCacheBuilder id( String id ) {
		transacao.setId( id );
		return this;
	}
	
	public TransacaoCacheBuilder contaOrigemId( long contaOrigemId ) {
		transacao.setOrigContaId( contaOrigemId );
		return this;
	}
	
	public TransacaoCacheBuilder contaDestinoId( long contaDestinoId ) {
		transacao.setDestContaId( contaDestinoId );
		return this;
	}
	
	public TransacaoCacheBuilder valor( double valor ) {
		transacao.setValor( valor );
		return this;
	}
		
	public TransacaoCacheBuilder tipo( TransacaoTipo tipo ) {
		transacao.setTipo( tipo );
		return this;
	}
	
}

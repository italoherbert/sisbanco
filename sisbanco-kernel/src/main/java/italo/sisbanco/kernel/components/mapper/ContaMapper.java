package italo.sisbanco.kernel.components.mapper;

import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Constantes;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;

@Component
public class ContaMapper {
	
	public void carregaParaRegistroInicial( Conta c, ContaSaveRequest req, String userId ) {
		c.setTitular( req.getTitular() );
		c.setUsername( req.getUser().getUsername() ); 
		c.setUserId( userId ); 
		c.setSaldo( 0 );
		c.setCredito( 0 ); 
		c.setLimiteOperacao( Constantes.LIMITE_OPERACAO_INICIAL );
		c.setLimiteDiario( Constantes.LIMITE_DIARIO_INICIAL ); 
	}
	
	public void carregaParaAlteracaoSimplificada( Conta c, ContaSaveRequest req ) {
		c.setTitular( req.getTitular() );		
	}
	
	public void carregaResponse( ContaResponse resp, Conta c ) {
		resp.setId( c.getId() );
		resp.setUsername( c.getUsername() ); 
		resp.setTitular( c.getTitular() );
		resp.setSaldo( c.getSaldo() );
		resp.setCredito( c.getCredito() );
		resp.setLimiteOperacao( c.getLimiteOperacao() );
		resp.setLimiteDiario( c.getLimiteDiario() ); 
	}
	
	public Conta novoBean() {
		return new Conta();
	}
	
	public ContaResponse novoContaResponse() {
		return new ContaResponse();
	}

}

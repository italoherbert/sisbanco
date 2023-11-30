package italo.sisbanco.kernel.service.mapper;

import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;

@Component
public class ContaMapper {
	
	public void carregaParaRegistro( Conta c, ContaSaveRequest req ) {
		c.setTitular( req.getTitular() );
		c.setUsername( req.getUser().getUsername() ); 
		c.setSaldo( 0 );
		c.setCredito( 0 ); 
		c.setSemAutorizacaoDebitoLimite( 0 );
	}
	
	public void carregaParaAlteracao( Conta c, ContaSaveRequest req ) {
		c.setTitular( req.getTitular() );		
	}
	
	public void carregaResponse( ContaResponse resp, Conta c ) {
		resp.setId( c.getId() );
		resp.setUsername( c.getUsername() ); 
		resp.setTitular( c.getTitular() );
		resp.setSaldo( c.getSaldo() );
		resp.setCredito( c.getCredito() );
		resp.setSemAutorizacaoDebitoLimite( c.getSemAutorizacaoDebitoLimite() ); 
	}
	
	public Conta novoBean() {
		return new Conta();
	}
	
	public ContaResponse novoContaResponse() {
		return new ContaResponse();
	}

}

package italo.sisbanco.mapper;

import org.springframework.stereotype.Component;

import italo.sisbanco.model.Conta;
import italo.sisbanco.model.request.conta.ContaSaveRequest;
import italo.sisbanco.model.response.conta.ContaResponse;

@Component
public class ContaMapper {
	
	public void carrega( Conta c, ContaSaveRequest req ) {
		c.setTitular( req.getTitular() );
		c.setSaldo( 0 );
		c.setCredito( 0 ); 
	}
	
	public void carregaResponse( ContaResponse resp, Conta c ) {
		resp.setTitular( c.getTitular() );
		resp.setSaldo( c.getSaldo() );
		resp.setCredito( c.getCredito() );
	}
	
	public Conta novoBean() {
		return new Conta();
	}
	
	public ContaResponse novoContaResponse() {
		return new ContaResponse();
	}

}

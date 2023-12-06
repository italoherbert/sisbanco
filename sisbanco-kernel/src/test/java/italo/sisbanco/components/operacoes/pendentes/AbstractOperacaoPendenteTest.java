package italo.sisbanco.components.operacoes.pendentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.redis.RedisTest;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;

public abstract class AbstractOperacaoPendenteTest extends RedisTest {

	@Autowired
	private ContaRepository contaRepository;
	
	protected abstract void configuraContaRegistro( Conta conta );
	
	protected abstract OperacaoPendenteResponse registraOperacaoEExecuta( Long contaId ) throws ServiceException;
	
	protected abstract void assertOpResponse( OperacaoPendenteResponse resp, Conta conta );
	
	@Test
	@ContaBD
	public void test() {				
		final Long CONTA_ID1 = 1L;
		
		Optional<Conta> contaOp = contaRepository.findById( CONTA_ID1 );
		assertTrue( contaOp.isPresent(), "Deveria ter sido encontrada a conta" );

		Conta conta = contaOp.get();
		this.configuraContaRegistro( conta ); 		
		contaRepository.save( conta );		
		
		try {
			OperacaoPendenteResponse resp = this.registraOperacaoEExecuta( CONTA_ID1 ); 
			
			contaOp = contaRepository.findById( CONTA_ID1 );
			assertTrue( contaOp.isPresent(), "Deveria ter sido encontrada a conta" );

			conta = contaOp.get();
			
			assertNotNull( resp.getConta(), "Conta nula." );
			assertEquals( resp.getConta().getId(), conta.getId(), "IDs das contas não correspondem." );			
			assertNotNull( resp.getDataOperacao(), "Data de operação não deveria ser nula." );
			assertTrue( resp.isRealizada(), "A operação deveria ter sido realizada. " );
			
			this.assertOpResponse( resp, conta );
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}		
	}
	
}

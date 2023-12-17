package italo.sisbanco.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.MockedFeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.MockedRabbitMQTestConfiguration;
import italo.sisbanco.ext.redis.RedisTest;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@SpringBootTest(classes=SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class TransacaoCacheRepositoryTest extends RedisTest {

	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;	
	
	@MockBean
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
				
	public void test1() {
		TransacaoCache tc; 
		
		transacaoCacheRepository.save( tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( 1 )
				.tipo( TransacaoTipo.DEBITO )
				.valor( 100 )				
				.get() ); 
		
		Optional<TransacaoCache> tcOp = transacaoCacheRepository.findById( tc.getId() );
		if ( !tcOp.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		TransacaoCache tc2 = tcOp.get();
		
		assertNotNull( tc.getOperacaoPendente(), "Operação pendente nula." );
		assertNotNull( tc2.getOperacaoPendente(), "Operação pendente nula." );

		assertEquals( tc2.getId(), tc.getId(), "IDs não correspondem." );
		assertEquals( tc2.getOperacaoPendente().getId(), tc.getOperacaoPendente().getId() );
		
		assertNotNull( tc2.getId(), "ID nulo" );
		assertNotNull( tc2.getOperacaoPendente().getId(), "ID de operação pendente nulo." );
		assertEquals( tc2.getOrigContaId(), 1, "Contas de origem não correspondem." );
		assertNotNull( tc2.getDataCriacao(), "Data de criação nula." );
		assertEquals( tc2.getTipo(), TransacaoTipo.DEBITO, "Tipos não correspondem." );
		assertEquals( tc2.getValor(), 100, "Valores não correspondem." );
		
		tcOp = transacaoCacheRepository.findByOperacaoPendenteId( tc.getOperacaoPendente().getId() );
		if ( !tcOp.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		tc2 = tcOp.get();
		
		assertNotNull( tc.getOperacaoPendente(), "Operação pendente nula." );
		assertNotNull( tc2.getOperacaoPendente(), "Operação pendente nula." );

		assertEquals( tc2.getId(), tc.getId(), "IDs não correspondem." );
		assertEquals( tc2.getOperacaoPendente().getId(), tc.getOperacaoPendente().getId() );
		
		assertNotNull( tc2.getId(), "ID nulo" );
		assertNotNull( tc2.getOperacaoPendente().getId(), "ID de operação pendente nulo." );
		assertEquals( tc2.getOrigContaId(), 1, "Contas de origem não correspondem." );
		assertNotNull( tc2.getDataCriacao(), "Data de criação nula." );
		assertEquals( tc2.getTipo(), TransacaoTipo.CREDITO, "Tipos não correspondem." );
		assertEquals( tc2.getValor(), 100, "Valores não correspondem." );
	}
	
	@Test
	public void test2() {
		final long CONTA_ID1 = 2;
		final long CONTA_ID2 = 3;
		final long CONTA_ID3 = 4;
		
		TransacaoCache tcache1 = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.tipo( TransacaoTipo.TRANSFERENCIA )
				.valor( 100 )				
				.get();
		
		TransacaoCache tcache2 = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID2 )
				.tipo( TransacaoTipo.DEBITO )
				.valor( 100 )				
				.get();
		
		TransacaoCache tcache3 = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.contaDestinoId( CONTA_ID3 ) 
				.tipo( TransacaoTipo.TRANSFERENCIA )
				.valor( 100 )				
				.get();
		
		transacaoCacheRepository.save( tcache1 );
		transacaoCacheRepository.save( tcache2 );
		transacaoCacheRepository.save( tcache3 );
						
		List<TransacaoCache> transacoes = transacaoCacheRepository.findByContaId( CONTA_ID1 );
		assertEquals( transacoes.size(), 2, "Número de transações encontrada não corresponde ao esperado." );
				
		transacoes = transacaoCacheRepository.findByContaId( CONTA_ID2 );		
		assertEquals( transacoes.size(), 1, "Deveria ter apenas uma transação registrada para esta conta." );
		
		TransacaoCache tcache6 = transacoes.get( 0 );
		tcacheAssertEquals( tcache6, tcache2 ); 
		
		tcache2.setValor( 1000 );
		
		transacaoCacheRepository.save( tcache2 );
		
		Optional<TransacaoCache> tcache7Op = transacaoCacheRepository.findById( tcache2.getId() );
		if ( !tcache7Op.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		TransacaoCache tcache7 = tcache7Op.get();
		
		tcacheAssertEquals( tcache7, tcache2 );
					
		boolean existe1 = transacaoCacheRepository.existsById( tcache1.getId() );
		boolean existe2 = transacaoCacheRepository.existsById( tcache2.getId() );
		boolean existe3 = transacaoCacheRepository.existsById( tcache3.getId() );

		assertTrue( existe1, "Transação deveria ser encontrada." );
		assertTrue( existe2, "Transação deveria ser encontrada." );
		assertTrue( existe3, "Transação deveria ser encontrada." );
		
		boolean existe4 = transacaoCacheRepository.existsById( "-1" );
		assertFalse( existe4, "Não deveria ter sido encontrada transação com esse ID." );
		
		transacaoCacheRepository.deleteById( tcache1.getId() );
		transacaoCacheRepository.deleteById( tcache2.getId() );
		transacaoCacheRepository.deleteById( tcache3.getId() );
		
		Optional<TransacaoCache> tcache1Op = transacaoCacheRepository.findById( tcache1.getId() );		
		Optional<TransacaoCache> tcache2Op = transacaoCacheRepository.findById( tcache1.getId() );		
		Optional<TransacaoCache> tcache3Op = transacaoCacheRepository.findById( tcache1.getId() );		
		
		assertFalse( tcache1Op.isPresent(), "Transacao não removida." );
		assertFalse( tcache2Op.isPresent(), "Transacao não removida." );
		assertFalse( tcache3Op.isPresent(), "Transacao não removida." );

		List<TransacaoCache> transacoes1 = transacaoCacheRepository.findByContaId( CONTA_ID1 );
		List<TransacaoCache> transacoes2 = transacaoCacheRepository.findByContaId( CONTA_ID2 );
		List<TransacaoCache> transacoes3 = transacaoCacheRepository.findByContaId( CONTA_ID3 );
		
		assertTrue( transacoes1.isEmpty(), "Número de transações diferente do esperado." );		
		assertTrue( transacoes2.isEmpty(), "Número de transações diferente do esperado." );		
		assertTrue( transacoes3.isEmpty(), "Número de transações diferente do esperado." );		
	}
	
	private void tcacheAssertEquals( TransacaoCache tc1, TransacaoCache tc2 ) {
		assertNotNull( tc1.getOperacaoPendente(), "A operação pendente da transação não deveria ser nula." );								
		assertEquals( tc1.getOrigContaId(), tc2.getOrigContaId(), "IDs de contas de origem diferentes." );
		assertEquals( tc1.getDestContaId(), tc2.getDestContaId(), "IDs de contas de destino diferentes." );
		assertEquals( tc1.getOperacaoPendente().getTipo(), tc2.getOperacaoPendente().getTipo(), "Tipos de operação diferentes." );
		assertEquals( tc1.getDataCriacao(), tc2.getDataCriacao(), "Datas de criação diferentes." );
		assertEquals( tc1.getTipo(), tc2.getTipo(), "Tipos diferentes." );
		assertEquals( tc1.getValor(), tc2.getValor(), "Valores diferentes." );
	}
	
}

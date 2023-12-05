package italo.sisbanco.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import italo.sisbanco.ext.openfeign.FeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.RabbitMQTestConfiguration;
import italo.sisbanco.ext.redis.RedisTest;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.operacoes.pendentes.OperacaoPendenteExecutor;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.model.builder.AlteraValorEmContaCacheBuilder;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@ActiveProfiles("test")
@SpringBootTest(classes=SisbancoKernelApplication.class)
@Import({
	RabbitMQTestConfiguration.class, 
	FeignClientsTestConfiguration.class
})
public class AlterValorEmContaCacheRepositoryTest extends RedisTest {

	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
		
	@MockBean
	private OperacaoPendenteExecutor operacaoPendenteExecutor;
	
	@MockBean
	private OperTransacaoCacheRepository transacaoCacheRepository;
				
	@Test
	public void test() {
		final long CONTA_ID1 = 2;
		final long CONTA_ID2 = 3;
		
		AlteraValorEmContaCache tcache1 = AlteraValorEmContaCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.dataOperacao( new Date() )
				.tipo( AlteraValorEmContaTipo.CREDITO )
				.valor( 100 )				
				.get();
		
		AlteraValorEmContaCache tcache2 = AlteraValorEmContaCacheBuilder.builder()
				.contaOrigemId( CONTA_ID2 )
				.dataOperacao( new Date() )
				.tipo( AlteraValorEmContaTipo.CREDITO )
				.valor( 100 )				
				.get();
		
		AlteraValorEmContaCache tcache3 = AlteraValorEmContaCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.dataOperacao( new Date() )
				.tipo( AlteraValorEmContaTipo.DEBITO_SIMPLES_LIMITE )
				.valor( 100 )				
				.get();
		
		alteraValorEmContaCacheRepository.save( tcache1 );
		alteraValorEmContaCacheRepository.save( tcache2 );
		alteraValorEmContaCacheRepository.save( tcache3 );
						
		List<AlteraValorEmContaCache> transacoes = alteraValorEmContaCacheRepository.findByContaId( CONTA_ID1 );
		assertEquals( transacoes.size(), 2, "Número de transações encontrada não corresponde ao esperado." );
				
		transacoes = alteraValorEmContaCacheRepository.findByContaId( CONTA_ID2 );		
		assertEquals( transacoes.size(), 1, "Deveria ter apenas uma transação registrada para esta conta." );
		
		AlteraValorEmContaCache tcache6 = transacoes.get( 0 );
		alterValorEmContaCacheAssertEquals( tcache6, tcache2 ); 
		
		tcache2.setValor( 1000 );
		
		alteraValorEmContaCacheRepository.save( tcache2 );
		
		Optional<AlteraValorEmContaCache> tcache7Op = alteraValorEmContaCacheRepository.findById( tcache2.getId() );
		if ( !tcache7Op.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		AlteraValorEmContaCache tcache7 = tcache7Op.get();
		
		alterValorEmContaCacheAssertEquals( tcache7, tcache2 );
					
		boolean existe1 = alteraValorEmContaCacheRepository.existsById( tcache1.getId() );
		boolean existe2 = alteraValorEmContaCacheRepository.existsById( tcache2.getId() );
		boolean existe3 = alteraValorEmContaCacheRepository.existsById( tcache3.getId() );

		assertTrue( existe1, "Transação deveria ser encontrada." );
		assertTrue( existe2, "Transação deveria ser encontrada." );
		assertTrue( existe3, "Transação deveria ser encontrada." );
		
		boolean existe4 = alteraValorEmContaCacheRepository.existsById( "-1" );
		assertFalse( existe4, "Não deveria ter sido encontrada transação com esse ID." );
		
		alteraValorEmContaCacheRepository.deleteById( tcache1.getId() );
		alteraValorEmContaCacheRepository.deleteById( tcache2.getId() );
		alteraValorEmContaCacheRepository.deleteById( tcache3.getId() );
		
		Optional<AlteraValorEmContaCache> tcache1Op = alteraValorEmContaCacheRepository.findById( tcache1.getId() );		
		Optional<AlteraValorEmContaCache> tcache2Op = alteraValorEmContaCacheRepository.findById( tcache1.getId() );		
		Optional<AlteraValorEmContaCache> tcache3Op = alteraValorEmContaCacheRepository.findById( tcache1.getId() );		
		
		assertFalse( tcache1Op.isPresent(), "Transacao não removida." );
		assertFalse( tcache2Op.isPresent(), "Transacao não removida." );
		assertFalse( tcache3Op.isPresent(), "Transacao não removida." );

		List<AlteraValorEmContaCache> transacoes1 = alteraValorEmContaCacheRepository.findByContaId( CONTA_ID1 );
		List<AlteraValorEmContaCache> transacoes2 = alteraValorEmContaCacheRepository.findByContaId( CONTA_ID2 );
		
		assertTrue( transacoes1.isEmpty(), "Número de transações diferente do esperado." );		
		assertTrue( transacoes2.isEmpty(), "Número de transações diferente do esperado." );		
	}
	
	private void alterValorEmContaCacheAssertEquals( AlteraValorEmContaCache tc1, AlteraValorEmContaCache tc2 ) {
		assertNotNull( tc1.getOperacaoPendente(), "A operação pendente da transação não deveria ser nula." );								
		assertEquals( tc1.getContaId(), tc2.getContaId(), "IDs de contas de origem diferentes." );
		assertEquals( tc1.getOperacaoPendente().getTipo(), tc2.getOperacaoPendente().getTipo(), "Tipos de operação diferentes." );
		assertEquals( tc1.getDataOperacao(), tc2.getDataOperacao(), "Datas de operação diferentes." );
		assertEquals( tc1.getTipo(), tc2.getTipo(), "Tipos diferentes." );
		assertEquals( tc1.getValor(), tc2.getValor(), "Valores diferentes." );
	}
	
}

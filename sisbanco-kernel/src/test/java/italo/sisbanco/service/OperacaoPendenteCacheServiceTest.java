package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.RedisPostgreSQLTest;
import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.FeignClientsTestConfiguration;
import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.rabbitmq.RabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.builder.AlteraValorEmContaCacheBuilder;
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;
import italo.sisbanco.kernel.service.OperacaoPendenteCacheService;

@SpringBootTest(classes=SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	RabbitMQTestConfiguration.class, 
	FeignClientsTestConfiguration.class
})
public class OperacaoPendenteCacheServiceTest extends RedisPostgreSQLTest {

	@Autowired
	private OperacaoPendenteCacheService operacaoPendenteService;
	
	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorCacheRepository;
		
	@Autowired
	private ContaRepository contaRepository;
	
	@AfterEach
	public void tearDown() {
		transacaoCacheRepository.deleteAll();
		alteraValorCacheRepository.deleteAll();
	}
	
	@Test
	@ContaBD
	public void testExec() {		
		final long CONTA_ID1 = 1;
		
		Optional<Conta> contaOp = contaRepository.findById( CONTA_ID1 );
		assertTrue( contaOp.isPresent(), "Conta não encontrada pelo ID="+CONTA_ID1 );
		
		Conta conta = contaOp.get();
		conta.setSaldo( 1000 );
		contaRepository.save( conta );
				
		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.tipo( TransacaoTipo.DEBITO )
				.valor( 100 )				
				.get();
		
		transacaoCacheRepository.save( tc );
		
		Optional<TransacaoCache> tcOp = transacaoCacheRepository.findById( tc.getId() );
		assertTrue( tcOp.isPresent(), "Transação não encontrada como operação pendente na cache." );
		
		tc = tcOp.get();
		assertNotNull( tc.getOperacaoPendente(), "Operação pendente nula." );
		assertNotNull( tc.getOperacaoPendente().getId(), "ID de operação pendente nulo." );
		
		String operId = tc.getOperacaoPendente().getId();
		
		try {
			OperacaoPendenteResponse resp = operacaoPendenteService.executa( operId );
			assertEquals( resp.getStatus(), OperacaoPendenteStatus.REALIZADA, "Operação deveria ter sido realizada." );						
		} catch (ErrorException e) {			
			e.printStackTrace();
			fail( "Erro= "+e.getErrorChave() );
		}		
	}	
	
	public void testOperacaoInvalida() {
		final long CONTA_ID1 = 1;
		
		Optional<Conta> contaOp = contaRepository.findById( CONTA_ID1 );
		assertTrue( contaOp.isPresent(), "Conta não encontrada pelo ID="+CONTA_ID1 );
		
		Conta conta = contaOp.get();
		conta.setSaldo( 1000 );
		contaRepository.save( conta );
				
		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.tipo( TransacaoTipo.CREDITO )
				.valor( 100 )				
				.get();
		
		try {
			operacaoPendenteService.executa( tc.getOperacaoPendente().getId() );
			fail( "Deveria lançar exceção. Tipo de transação inválido!" );
		} catch (ErrorException e) {
			
		}
	}
	
	@Test
	@ContaBD
	public void testGet() {
		final long CONTA_ID1 = 1;

		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.tipo( TransacaoTipo.CREDITO )
				.valor( 100 )				
				.get();
		
		transacaoCacheRepository.save( tc );
		
		String operId = tc.getOperacaoPendente().getId();
		assertNotNull( operId, "O ID da operação não deveria ser nulo." );
		
		try {
			System.out.println( operId );
			operacaoPendenteService.get( operId );			
		} catch (ErrorException e) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		} 
	}	
	
	@Test
	@ContaBD
	public void testLista() {
		final long CONTA_ID1 = 1;
		
		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( CONTA_ID1 )
				.tipo( TransacaoTipo.CREDITO )
				.valor( 100 )				
				.get();
		
		AlteraValorEmContaCache alterValor = AlteraValorEmContaCacheBuilder.builder()
				.contaId( CONTA_ID1 )
				.tipo( AlteraValorEmContaTipo.CREDITO )
				.valor( 200 )				
				.get();
		
		transacaoCacheRepository.save( tc );
		alteraValorCacheRepository.save( alterValor );
				
		try {
			List<OperacaoPendenteResponse> lista = operacaoPendenteService.listaPorConta( CONTA_ID1 );
			assertEquals( lista.size(), 2, "Deveriam haver duas operações em cache." );
			
			for( OperacaoPendenteResponse resp : lista )
				assertEquals( resp.getConta().getId(), CONTA_ID1, "ID da conta está incorreto." );			
		} catch (ErrorException e) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		}	
	}
		
}

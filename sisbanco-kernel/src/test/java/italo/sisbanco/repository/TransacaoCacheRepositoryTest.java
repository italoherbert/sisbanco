package italo.sisbanco.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import italo.sisbanco.config.redis.TestRedisConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import italo.sisbanco.kernel.repository.TransacaoCacheRepository;

@SpringBootTest(classes= { SisbancoKernelApplication.class } )
@ActiveProfiles("test")
@Import({TestRedisConfiguration.class})
public class TransacaoCacheRepositoryTest {

	@Autowired
	private TransacaoCacheRepository transacaoCacheRepository;
	
	@Test
	public void test() {
		TransacaoCache tcache = new TransacaoCache();
		tcache.setOrigContaId( 2L );
		tcache.setDestContaId( 0L );
		tcache.setDataOperacao( new Date() );
		tcache.setTipo( TransacaoTipo.CREDITO );
		tcache.setValor( 100 ); 

		transacaoCacheRepository.save( tcache );
		
		String id = tcache.getId();
		
		Optional<TransacaoCache> tcache2Op = transacaoCacheRepository.findById( id );
		if ( !tcache2Op.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		TransacaoCache tcache2 = tcache2Op.get();
								
		assertEquals( tcache.getOrigContaId(), tcache2.getOrigContaId(), "IDs de contas de origem diferentes." );
		assertEquals( tcache.getDestContaId(), tcache2.getDestContaId(), "IDs de contas de destino diferentes." );
		assertEquals( tcache.getDataOperacao(), tcache2.getDataOperacao(), "Datas de operação diferentes." );
		assertEquals( tcache.getTipo(), tcache2.getTipo(), "Tipos diferentes." );
		assertEquals( tcache.getValor(), tcache2.getValor(), "Valores diferentes." );
		
		tcache2.setValor( 1000 );
		
		transacaoCacheRepository.save( tcache2 );
		
		Optional<TransacaoCache> tcache3Op = transacaoCacheRepository.findById( id );
		if ( !tcache3Op.isPresent() )
			fail( "Transação que foi registrada não foi encontrada em cache." );
		
		TransacaoCache tcache3 = tcache2Op.get();
		
		assertEquals( tcache2.getValor(), tcache3.getValor(), "Valores diferentes." );
			
		List<TransacaoCache> transacoes = transacaoCacheRepository.findByContaId( tcache3.getOrigContaId() );
		assertEquals( transacoes.size(), 1, "Número de transações diferente do esperado." );
		
		boolean existe = transacaoCacheRepository.existsById( tcache3.getId() );
		assertTrue( existe, "Transação deveria ser encontrada." );
		
		existe = transacaoCacheRepository.existsById( "-1" );
		assertFalse( existe, "Não deveria ter sido encontrada transação com esse ID." );
		
		transacaoCacheRepository.deleteById( tcache3.getId() );
		
		Optional<TransacaoCache> tcache4Op = transacaoCacheRepository.findById( tcache3.getId() );		
		assertFalse( tcache4Op.isPresent(), "Transacao não removida." );

		transacoes = transacaoCacheRepository.findByContaId( tcache3.getOrigContaId() );
		assertTrue( transacoes.isEmpty(), "Número de transações diferente do esperado." );		
	}
	
}

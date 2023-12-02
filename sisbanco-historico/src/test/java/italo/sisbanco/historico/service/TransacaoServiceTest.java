package italo.sisbanco.historico.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import italo.sisbanco.historico.SisbancoHistoricoApplication;
import italo.sisbanco.historico.config.RabbitTestConfiguration;
import italo.sisbanco.historico.exception.ErrorException;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.message.TransacaoMessage;

@Testcontainers
@SpringBootTest(
		classes=SisbancoHistoricoApplication.class, 
		webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@Import(RabbitTestConfiguration.class)
public class TransacaoServiceTest {
		
	@Autowired
	private TransacaoService transacaoService;
			
	private static MongoDBContainer mongoContainer;
	
	static {
		mongoContainer = new MongoDBContainer( DockerImageName.parse( "mongo" ) );
		mongoContainer.start();
	}
	
	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
	}
	
	@Test
	public void test2() {
		for( int i = 0; i < 10; i++ ) {
			TransacaoMessage tm = new TransacaoMessage();
			tm.setUsername( "abc" );
			tm.setTipo( "CREDITO" );
			tm.setValor( 100 );
			tm.setDataOperacao( new Date() );
		}
	}
	
	@Test
	public void test() {
		TransacaoMessage tm = new TransacaoMessage();
		tm.setUsername( "abc" );
		tm.setTipo( "CREDITO" );
		tm.setValor( 100 );
		tm.setDataOperacao( new Date() );
		
		Transacao t = transacaoService.registraTransacao( tm );
		
		String tid = t.getId();
		
		t = transacaoService.get( tid );
		assertEquals( t.getUsername(), tm.getUsername() );
		assertEquals( t.getValor(), tm.getValor() );
		assertEquals( t.getDataOperacao(), tm.getDataOperacao() );
		assertEquals( t.getTipo(), tm.getTipo() );
		
		transacaoService.deleta( tid );
				
		try {
			t = transacaoService.get( tid );
			fail( "Deveria lançar exceção porque a transação já foi removida." );
		} catch ( ErrorException e ) {
			
		}
	}
		
}
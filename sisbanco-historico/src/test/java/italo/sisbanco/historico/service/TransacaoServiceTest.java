package italo.sisbanco.historico.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import italo.sisbanco.historico.SisbancoHistoricoApplication;
import italo.sisbanco.historico.exception.ErrorException;
import italo.sisbanco.historico.exception.TransacaoRabbitListenerErrorHandler;
import italo.sisbanco.historico.ext.mongodb.MongoDBTest;
import italo.sisbanco.historico.ext.rabbitmq.RabbitTestConfiguration;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.message.TransacaoMessage;
import italo.sisbanco.historico.service.message.TransacaoMessageService;

@SpringBootTest(classes=SisbancoHistoricoApplication.class)
@Import(RabbitTestConfiguration.class)
public class TransacaoServiceTest extends MongoDBTest {
		
	@Autowired
	private TransacaoService transacaoService;
	
	@MockBean
	private TransacaoMessageService transacaoMessageService;
	
	@MockBean
	private TransacaoRabbitListenerErrorHandler transacaoRabbitListenerErrorHandler;
	
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
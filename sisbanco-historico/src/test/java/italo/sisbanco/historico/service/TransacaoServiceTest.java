package italo.sisbanco.historico.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;

import italo.sisbanco.historico.config.RabbitTestConfiguration;
import italo.sisbanco.historico.exception.ErrorException;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.message.TransacaoMessage;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@Import(RabbitTestConfiguration.class)
public class TransacaoServiceTest {
	
	@Autowired
	private TransacaoService transacaoService;
			
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
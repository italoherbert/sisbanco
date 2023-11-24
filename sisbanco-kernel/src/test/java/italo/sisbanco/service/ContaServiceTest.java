package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import italo.sisbanco.exception.ServiceException;
import italo.sisbanco.model.request.conta.ValorRequest;
import italo.sisbanco.model.response.conta.ContaResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContaServiceTest {
		
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private BancoService bancoService;
			
	@Test	
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( titular, username, saldo, credito ) values ( 'joao', 'joao', 0, 0 )",
	})
	@Sql(scripts = {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void creditoTest() {				
		try {					
			this.executaCredito( "joao", 300 );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test	
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( titular, username, saldo, credito ) values ( 'joao', 'joao', 300, 500 )",
	})
	@Sql(scripts = {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void debitoTest() {		
		try {					
			this.executaDebito( "joao", 200 );
			
			try {
				this.executaDebito( "joao", 1000 );
				fail( "Deveria ter lançado exceção. ");
			} catch ( ServiceException e ) {
				
			}
			
			this.executaDebito( "joao", 600 );			
			assertEquals( this.getSaldo( "joao" ), -500, "Saldo incorreto. " );
			
			try {
				this.executaDebito( "joao", 0.1 );
				fail( "Deveria ter lançado exceção. ");
			} catch ( ServiceException e ) {
				
			}			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test	
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( titular, username, saldo, credito ) values ( 'joao', 'joao', 300, 500 )",
		"insert into conta ( titular, username, saldo, credito ) values ( 'maria', 'maria', 0, 500 )",
	})
	@Sql(scripts = {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void transferenciaTest() {		
		try {					
			this.executaTransferencia( "joao", "maria", 200 );
			
			try {
				this.executaTransferencia( "joao", "maria", 700 );
				fail( "Transferencia realizada sem saldo suficiente." );
			} catch ( ServiceException e ) {
				
			}
			
			this.executaTransferencia( "joao", "maria", 600 );
			
			try {
				this.executaTransferencia( "joao", "maria", 0.1 );
				fail( "Transferencia realizada sem saldo suficiente." );
			} catch ( ServiceException e ) {
				
			}
			
			assertEquals( this.getSaldo( "joao" ), -500, "Saldo inválido para titular joão." );
			assertEquals( this.getSaldo( "maria" ), 800, "Saldo inválido para titular maria." );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void executaCredito( String username, double valor ) throws ServiceException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );
		
		ValorRequest credito = new ValorRequest();
		credito.setValor( valor );			
		
		bancoService.credita( conta.getId(), credito );

		ContaResponse conta2 = contaService.get( conta.getId() );
		assertEquals( conta2.getSaldo(), conta.getSaldo() + valor, "Valor não creditado." ); 
	}
	
	private void executaDebito( String username, double valor ) throws ServiceException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );			
		
		double saldo = conta.getSaldo();
		
		ValorRequest debito = new ValorRequest();
		debito.setValor( valor );
		
		bancoService.debita( conta.getId(), debito );
		
		ContaResponse conta2 = contaService.get( conta.getId() );
		assertEquals( conta2.getSaldo(), saldo-valor, "Valor não debitado corretamente." );		
	}
	
	private void executaTransferencia( String origUsername, String destUsername, double valor ) throws ServiceException {
		ContaResponse contaOrig = contaService.getByUsername( origUsername );		
		ContaResponse contaDest = contaService.getByUsername( destUsername );
		
		assertNotNull( contaOrig, "Conta não encontrada. Username="+origUsername );					
		assertNotNull( contaDest, "Conta não encontrada. Username="+destUsername );			
		
		double saldoOrig = contaOrig.getSaldo();
		double saldoDest = contaDest.getSaldo();
				
		ValorRequest transferencia = new ValorRequest();
		transferencia.setValor( valor );
		
		bancoService.transfere( contaOrig.getId(), contaDest.getId(), transferencia ); 
		
		ContaResponse contaOrig2 = contaService.getByUsername( origUsername );		
		ContaResponse contaDest2 = contaService.getByUsername( destUsername );

		double saldoOrig2 = contaOrig2.getSaldo();
		double saldoDest2 = contaDest2.getSaldo();
		
		assertEquals( saldoOrig2, saldoOrig-valor, "Saldo inválido para conta de origem." );
		assertEquals( saldoDest2, saldoDest+valor, "Saldo inválido para conta de destino." );		
	}
	
	private double getSaldo( String username ) throws ServiceException {
		ContaResponse resp = contaService.getByUsername( username );
		assertNotNull( resp, "Não foi possível encontrar a conta pelo username: "+username );
		
		return resp.getSaldo();
	}
	
}

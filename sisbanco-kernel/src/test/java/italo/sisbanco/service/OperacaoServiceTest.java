package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.RedisPostgreSQLTest;
import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.MockedFeignClientsTestConfiguration;
import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.rabbitmq.MockedRabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.messageria.TransacaoMessageSender;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.service.ContaService;
import italo.sisbanco.kernel.service.OperacaoService;

@SpringBootTest(classes=SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class OperacaoServiceTest extends RedisPostgreSQLTest {
		
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private OperacaoService operacaoService;		
		
	@MockBean
	private TransacaoMessageSender transacaoMessageSender;
	
	@Test	
	@ContaBD
	public void creditoTest() {				
		try {					
			this.alteraSaldo( "joao", 0 );
			this.alteraCredito( "joao", 0 );
			this.alteraDebitoSimplesLimite( "joao", 1000 ); 
									
			OperacaoPendenteResponse resp = this.executaCredito( "joao", 300 );
			assertEquals( resp.getStatus(), OperacaoPendenteStatus.REALIZADA, "CREDITO: Transação deveria ter sido realizada com sucessso." );						
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test	
	@ContaBD
	public void debitoTest() {			
		try {					
			this.alteraSaldo( "joao", 300 );
			this.alteraCredito( "joao", 500 );
			this.alteraDebitoSimplesLimite( "joao", 1000 ); 
									
			this.executaDebito( "joao", 200 );
			
			this.executaDebito( "joao", 1001 );				

			try {
				this.executaDebito( "joao", 601 );
				fail( "Deveria ter lançado exceção. ");
			} catch ( ErrorException e ) {
				
			}
			
			this.executaDebito( "joao", 600 );			
			assertEquals( this.getSaldo( "joao" ), -500, "Saldo incorreto. " );
			
			try {
				this.executaDebito( "joao", 0.1 );
				fail( "Deveria ter lançado exceção. ");
			} catch ( ErrorException e ) {
				
			}			

			verify( transacaoMessageSender, times( 2 ) ).envia( any(Conta.class), anyDouble(), any(TransacaoTipo.class) );
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		}
	}
	
	@Test	
	@ContaBD
	public void transferenciaTest() {				
		try {					
			this.alteraSaldo( "joao", 300 );
			this.alteraCredito( "joao", 500 );
			this.alteraDebitoSimplesLimite( "joao", 1000 ); 
			
			this.alteraSaldo( "maria", 0 );
			this.alteraCredito( "maria", 0 );
			this.alteraDebitoSimplesLimite( "maria", 1000 ); 
			
			this.executaTransferencia( "joao", "maria", 200 );
			
			this.executaTransferencia( "joao", "maria", 1001 );
							
			try {
				this.executaTransferencia( "joao", "maria", 700 );				
				fail( "Transferencia realizada sem saldo suficiente." );
			} catch ( ErrorException e ) {
				
			}
			
			this.executaTransferencia( "joao", "maria", 600 );
			
			try {
				this.executaTransferencia( "joao", "maria", 0.1 );
				fail( "Transferencia realizada sem saldo suficiente." );
			} catch ( ErrorException e ) {
				
			}
								
			assertEquals( this.getSaldo( "joao" ), -500, "Saldo inválido para titular joão." );
			assertEquals( this.getSaldo( "maria" ), 800, "Saldo inválido para titular maria." );

			verify( transacaoMessageSender, times( 2 ) ).envia( any(Conta.class), anyDouble(), any(TransacaoTipo.class) );
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );			
		}
	}
	
	@Test
	@ContaBD
	public void testAlteraSaldo() {
		try {					
			this.alteraSaldo( "joao", 300 );
			this.alteraSaldo( "maria", 800 ); 
													
			assertEquals( this.getSaldo( "joao" ), 300, "Saldo inválido para titular joão." );
			assertEquals( this.getSaldo( "maria" ), 800, "Saldo inválido para titular maria." );
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		}
	}
	
	@Test
	@ContaBD
	public void testAlteraCredito() {
		try {					
			this.alteraCredito( "joao", 300 );
			this.alteraCredito( "maria", 800 ); 
			
			try {
				this.alteraCredito( "joao", -1 );
				fail( "Deveria lançar exceção. Crédito negativo." );
			} catch ( ErrorException e ) {
				
			}
			
			ContaResponse joaoConta = this.getContaByUsername( "joao" );
			ContaResponse mariaConta = this.getContaByUsername( "maria" );
													
			assertEquals( joaoConta.getCredito(), 300, "Crédito inválido para titular joão." );
			assertEquals( mariaConta.getCredito(), 800, "Crédito inválido para titular maria." );
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		}
	}
	
	@Test
	@ContaBD
	public void testAlteraDebitoSimplesLimite() {
		try {					
			this.alteraDebitoSimplesLimite( "joao", 300 );
			this.alteraDebitoSimplesLimite( "maria", 800 ); 
			
			try {
				this.alteraDebitoSimplesLimite( "joao", -1 );
				fail( "Deveria lançar exceção. Crédito negativo." );
			} catch ( ErrorException e ) {
				
			}
			
			ContaResponse joaoConta = this.getContaByUsername( "joao" );
			ContaResponse mariaConta = this.getContaByUsername( "maria" );
													
			assertEquals( joaoConta.getDebitoSimplesLimite(), 300, "Débito simples limite inválido para titular joão." );
			assertEquals( mariaConta.getDebitoSimplesLimite(), 800, "Débito simples limite inválido para titular maria." );
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		}
	}
	
	private OperacaoPendenteResponse executaCredito( String username, double valor ) throws ErrorException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );
		
		double saldo = conta.getSaldo();
		
		ValorRequest credito = new ValorRequest();
		credito.setValor( valor );			
		
		OperacaoPendenteResponse resp = operacaoService.credita( conta.getId(), credito );

		ContaResponse conta2 = contaService.get( conta.getId() );
		if ( resp.getStatus() == OperacaoPendenteStatus.REALIZADA ) {
			assertNotNull( resp.getConta(), "CREDITO: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "CREDITO: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldo, "CREDITO: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldo + valor, "CREDITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo + valor, "CREDITO: Saldo inconsistente." );
		} else {
			assertNotNull( resp.getConta(), "CREDITO: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "CREDITO: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldo, "CREDITO: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldo, "CREDITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo, "CREDITO: Saldo inconsistente." );			
		}
		
		return resp;
	}
	
	private OperacaoPendenteResponse executaDebito( String username, double valor ) throws ErrorException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );
		
		double saldo = conta.getSaldo();
				
		ValorRequest debito = new ValorRequest();
		debito.setValor( valor );
		
		OperacaoPendenteResponse resp = operacaoService.debita( conta.getId(), debito );
		
		ContaResponse conta2 = contaService.get( conta.getId() );
		
		if ( resp.getStatus() == OperacaoPendenteStatus.REALIZADA ) {
			assertNotNull( resp.getConta(), "DEBITO: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "DEBITO: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldo, "DEBITO: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldo - valor, "DEBITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo - valor, "DEBITO: Saldo inconsistente." );
		} else {
			assertNotNull( resp.getConta(), "DEBITO: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "DEBITO: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldo, "DEBITO: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldo, "DEBITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo, "DEBITO: Saldo inconsistente." );			
		}
		return resp;
	}
	
	private OperacaoPendenteResponse executaTransferencia( String origUsername, String destUsername, double valor ) throws ErrorException {
		ContaResponse contaOrig = contaService.getByUsername( origUsername );		
		ContaResponse contaDest = contaService.getByUsername( destUsername );
		
		assertNotNull( contaOrig, "Conta não encontrada. Username="+origUsername );					
		assertNotNull( contaDest, "Conta não encontrada. Username="+destUsername );			
		
		double saldoOrig = contaOrig.getSaldo();
		double saldoDest = contaDest.getSaldo();
				
		ValorRequest transferencia = new ValorRequest();
		transferencia.setValor( valor );
		
		OperacaoPendenteResponse resp = operacaoService.transfere( contaOrig.getId(), contaDest.getId(), transferencia ); 
					
		ContaResponse contaOrig2 = contaService.getByUsername( origUsername );		
		ContaResponse contaDest2 = contaService.getByUsername( destUsername );

		double saldoOrig2 = contaOrig2.getSaldo();
		double saldoDest2 = contaDest2.getSaldo();
				
		if ( resp.getStatus() == OperacaoPendenteStatus.REALIZADA ) {
			assertNotNull( resp.getConta(), "TRANSFERENCIA: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "TRANSFERENCIA: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldoOrig, "TRANSFERENCIA: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldoOrig - valor, "TRANSFERENCIA: Saldo atual incorreto." );
			assertEquals( saldoOrig2, saldoOrig-valor, "TRANSFERENCIA: Saldo da conta de origem inconsistente." );
			assertEquals( saldoDest2, saldoDest+valor, "TRANSFERENCIA: Saldo da conta de destino inconsistente." );		
		} else {
			assertNotNull( resp.getConta(), "TRANSFERENCIA: Conta não deveria ser nula." );
			assertEquals( resp.getValor(), valor, "TRANSFERENCIA: Valor não corresponde ao esperado.");
			assertEquals( resp.getSaldoAnterior(), saldoOrig, "TRANSFERENCIA: Saldo anterior incorreto." );
			assertEquals( resp.getConta().getSaldo(), saldoOrig, "TRANSFERENCIA: Saldo atual incorreto." );
			assertEquals( saldoOrig2, saldoOrig, "TRANSFERENCIA: Saldo da conta de origem inconsistente." );
			assertEquals( saldoDest2, saldoDest, "TRANSFERENCIA: Saldo da conta de destino inconsistente." );		
		}
				
		return resp;
	}
			
	private double getSaldo( String username ) throws ErrorException {
		ContaResponse resp = contaService.getByUsername( username );
		assertNotNull( resp, "Não foi possível encontrar a conta pelo username: "+username );
		
		return resp.getSaldo();
	}
		
	private void alteraCredito( String username, double credito ) throws ErrorException {
		ContaResponse resp = this.getContaByUsername( username );
		ValorRequest valor = new ValorRequest();
		valor.setValor( credito );		
		contaService.alteraCredito( resp.getId(), valor );
	}
	
	private void alteraDebitoSimplesLimite( String username, double debitoSimplesLimite ) throws ErrorException {
		ContaResponse resp = this.getContaByUsername( username );
		ValorRequest valor = new ValorRequest();
		valor.setValor( debitoSimplesLimite );		
		contaService.alteraDebitoSimplesLimite( resp.getId(), valor );
	}
	
	private void alteraSaldo( String username, double saldo ) throws ErrorException {
		ContaResponse resp = this.getContaByUsername( username );
		ValorRequest valor = new ValorRequest();
		valor.setValor( saldo );		
		contaService.alteraSaldo( resp.getId(), valor );
		contaService.alteraSaldo( resp.getId(), valor );
	}
	
	private ContaResponse getContaByUsername( String username ) throws ErrorException {
		ContaResponse resp = contaService.getByUsername( username );
		assertNotNull( resp, "Não foi possível encontrar a conta pelo username: "+username );
		return resp;
	}
	
}

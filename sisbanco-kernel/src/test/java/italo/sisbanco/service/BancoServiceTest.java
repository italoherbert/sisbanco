package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.postgresql.PostgreSQLTest;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.message.TransacaoMessageSender;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.TransacaoResponse;
import italo.sisbanco.kernel.repository.TransacaoCacheRepository;
import italo.sisbanco.kernel.service.BancoService;
import italo.sisbanco.kernel.service.ContaService;

@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BancoServiceTest extends PostgreSQLTest {
		
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private BancoService bancoService;		
				
	@MockBean
	private TransacaoMessageSender transacaoMessageSender;
	
	@MockBean
	private TransacaoCacheRepository transacaoCacheRepository;
				
	@Test	
	@ContaBD
	public void creditoTest() {				
		try {					
			this.alteraContaValores( "joao", 0, 0, 1000 );
						
			TransacaoResponse resp = this.executaCredito( "joao", 300 );
			assertTrue( resp.isRealizada(), "CREDITO: Transação deveria ter sido realizada com sucessso." );			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test	
	@ContaBD
	public void debitoTest() {			
		try {					
			this.alteraContaValores( "joao", 300, 500, 1000 );
						
			this.executaDebito( "joao", 200 );
			
			this.executaDebito( "joao", 1001 );				

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

			verify( transacaoMessageSender, times( 2 ) ).envia( any(Conta.class), anyDouble(), any(TransacaoTipo.class) );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test	
	@ContaBD
	public void transferenciaTest() {				
		try {					
			this.alteraContaValores( "joao", 300, 500, 1000 );
			this.alteraContaValores( "maria", 0, 0, 1000 );

			this.executaTransferencia( "joao", "maria", 200 );
			
			this.executaTransferencia( "joao", "maria", 1001 );
							
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

			verify( transacaoMessageSender, times( 2 ) ).envia( any(Conta.class), anyDouble(), any(TransacaoTipo.class) );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private TransacaoResponse executaCredito( String username, double valor ) throws ServiceException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );
		
		double saldo = conta.getSaldo();
		
		ValorRequest credito = new ValorRequest();
		credito.setValor( valor );			
		
		TransacaoResponse resp = bancoService.credita( conta.getId(), credito );

		ContaResponse conta2 = contaService.get( conta.getId() );
		if ( resp.isRealizada() ) {
			assertEquals( resp.getSaldoAnterior(), saldo, "CREDITO: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldo + valor, "CREDITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo + valor, "CREDITO: Saldo inconsistente." );
		} else {
			assertEquals( resp.getSaldoAnterior(), saldo, "CREDITO: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldo, "CREDITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo, "CREDITO: Saldo inconsistente." );			
		}
		
		return resp;
	}
	
	private TransacaoResponse executaDebito( String username, double valor ) throws ServiceException {
		ContaResponse conta = contaService.getByUsername( username );
		assertNotNull( conta, "Conta não encontrada. Username="+username );
		
		double saldo = conta.getSaldo();
				
		ValorRequest debito = new ValorRequest();
		debito.setValor( valor );
		
		TransacaoResponse resp = bancoService.debita( conta.getId(), debito );
		
		ContaResponse conta2 = contaService.get( conta.getId() );
		
		if ( resp.isRealizada() ) {
			assertEquals( resp.getSaldoAnterior(), saldo, "DEBITO: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldo - valor, "DEBITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo - valor, "DEBITO: Saldo inconsistente." );
		} else {
			assertEquals( resp.getSaldoAnterior(), saldo, "DEBITO: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldo, "DEBITO: Saldo atual incorreto." );
			assertEquals( conta2.getSaldo(), saldo, "DEBITO: Saldo inconsistente." );			
			verify( transacaoCacheRepository ).save( any( TransacaoCache.class ) ); 
		}
		return resp;
	}
	
	private TransacaoResponse executaTransferencia( String origUsername, String destUsername, double valor ) throws ServiceException {
		ContaResponse contaOrig = contaService.getByUsername( origUsername );		
		ContaResponse contaDest = contaService.getByUsername( destUsername );
		
		assertNotNull( contaOrig, "Conta não encontrada. Username="+origUsername );					
		assertNotNull( contaDest, "Conta não encontrada. Username="+destUsername );			
		
		double saldoOrig = contaOrig.getSaldo();
		double saldoDest = contaDest.getSaldo();
				
		ValorRequest transferencia = new ValorRequest();
		transferencia.setValor( valor );
		
		TransacaoResponse resp = bancoService.transfere( contaOrig.getId(), contaDest.getId(), transferencia ); 
			
		
		ContaResponse contaOrig2 = contaService.getByUsername( origUsername );		
		ContaResponse contaDest2 = contaService.getByUsername( destUsername );

		double saldoOrig2 = contaOrig2.getSaldo();
		double saldoDest2 = contaDest2.getSaldo();
				
		if ( resp.isRealizada() ) {
			assertEquals( resp.getSaldoAnterior(), saldoOrig, "TRANSFERENCIA: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldoOrig - valor, "TRANSFERENCIA: Saldo atual incorreto." );
			assertEquals( saldoOrig2, saldoOrig-valor, "TRANSFERENCIA: Saldo da conta de origem inconsistente." );
			assertEquals( saldoDest2, saldoDest+valor, "TRANSFERENCIA: Saldo da conta de destino inconsistente." );		
		} else {
			assertEquals( resp.getSaldoAnterior(), saldoOrig, "TRANSFERENCIA: Saldo anterior incorreto." );
			assertEquals( resp.getSaldoAtual(), saldoOrig, "TRANSFERENCIA: Saldo atual incorreto." );
			assertEquals( saldoOrig2, saldoOrig, "TRANSFERENCIA: Saldo da conta de origem inconsistente." );
			assertEquals( saldoDest2, saldoDest, "TRANSFERENCIA: Saldo da conta de destino inconsistente." );		
			verify( transacaoCacheRepository ).save( any( TransacaoCache.class ) ); 
		}
				
		return resp;
	}
	
	private double getSaldo( String username ) throws ServiceException {
		ContaResponse resp = contaService.getByUsername( username );
		assertNotNull( resp, "Não foi possível encontrar a conta pelo username: "+username );
		
		return resp.getSaldo();
	}
		
	private void alteraContaValores( String username, double saldo, double credito, double semAutorizacaoDebitoLimite ) throws ServiceException {
		ContaResponse resp = contaService.getByUsername( username );
		assertNotNull( resp, "Não foi possível encontrar a conta pelo username: "+username );
		
		ValorRequest saldoValor = new ValorRequest();
		saldoValor.setValor( saldo );		
		contaService.alteraSaldo( resp.getId(), saldoValor );
		
		ValorRequest creditoValor = new ValorRequest();
		creditoValor.setValor( credito );		
		contaService.alteraCredito( resp.getId(), creditoValor );
		
		ValorRequest semAutorizacaoDebitoLimiteValor = new ValorRequest();
		semAutorizacaoDebitoLimiteValor.setValor( semAutorizacaoDebitoLimite );		
		contaService.alteraSemAutorizacaoDebitoLimite( resp.getId(), semAutorizacaoDebitoLimiteValor );
	}
	
}

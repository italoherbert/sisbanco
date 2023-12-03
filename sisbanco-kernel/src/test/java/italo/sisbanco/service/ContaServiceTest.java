package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.github.dockerjava.api.model.Ports.Binding;
import com.rabbitmq.client.AMQP.Exchange;
import com.rabbitmq.client.AMQP.Queue;

import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.postgresql.PostgreSQLTest;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.message.TransacaoMessageSender;
import italo.sisbanco.kernel.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.repository.TransacaoCacheRepository;
import italo.sisbanco.kernel.service.ContaService;

@ActiveProfiles("test")
@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContaServiceTest extends PostgreSQLTest {

	@Autowired
	private ContaService contaService;
			
	@MockBean
	private TransacaoCacheRepository transacaoCacheRepository;
	
	@MockBean
	private TransacaoMessageSender transacaoMessageSender;
	
	@MockBean
	private Queue transacoesQueue;
	
	@MockBean
	private Exchange transacoesExchange;
	
	@MockBean
	private Binding transacoesBinding;
	
	@Test
	@ContaBD
	public void alteraTest() {
		String username = "maria";
		try {
			ContaResponse conta = contaService.getByUsername( username );
			assertNotNull( conta, "Conta não encontrada pelo username: "+username );
						
			ContaSaveRequest contaSave = new ContaSaveRequest();
			contaSave.setTitular( "mariano" );
			
			contaService.altera( conta.getId(), contaSave );			
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD
	public void getTest() {
		try {
			ContaResponse conta = contaService.get( 1L );
			assertNotNull( conta, "Conta não encontrada pelo ID: 1" );
			
			conta = contaService.get( 2L );
			assertNotNull( conta, "Conta não encontrada pelo ID: 2" );
			
			conta = contaService.get( 3L );
			assertNotNull( conta, "Conta não encontrada pelo ID: 3" );
			
			try {
				conta = contaService.get( -1L );
				fail( "Conta acessada com ID inválido." );
			} catch ( ServiceException e ) {
				
			}									
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD	
	public void getByUsernameTest() {
		try {
			ContaResponse conta = contaService.getByUsername( "jose" );
			assertNotNull( conta, "Conta não encontrada pelo username: jose" );
			
			conta = contaService.getByUsername( "joao" );
			assertNotNull( conta, "Conta não encontrada pelo username: joao" );
			
			conta = contaService.getByUsername( "maria" );
			assertNotNull( conta, "Conta não encontrada pelo username: maria" );
			
			try {
				conta = contaService.getByUsername( "abc" );
				fail( "Conta acessada com Username inválido." );
			} catch ( ServiceException e ) {
				
			}									
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD	
	public void alteraSaldoTest() {
		double valor = 300;
		try {
			ContaResponse conta = contaService.getByUsername( "jose" );
			
			ValorRequest valorReq = new ValorRequest();
			valorReq.setValor( valor ); 
			
			contaService.alteraSaldo( conta.getId(), valorReq );
			
			conta = contaService.getByUsername( "jose" );
			assertEquals( conta.getSaldo(), valor, "Saldo não alterado." );
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD	
	public void alteraCreditoTest() {
		double valor = 300;
		try {
			ContaResponse conta = contaService.getByUsername( "jose" );
			
			ValorRequest valorReq = new ValorRequest();
			valorReq.setValor( valor ); 
			
			contaService.alteraCredito( conta.getId(), valorReq );
			
			conta = contaService.getByUsername( "jose" );
			assertEquals( conta.getCredito(), valor, "Crédito não alterado." );
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD	
	public void filtraTest() {
		ContaFiltroRequest filtro = new ContaFiltroRequest();
		filtro.setTitular( "jo" ); 
		
		List<ContaResponse> contas = contaService.filtra( filtro );		
		assertEquals( contas.size(), 2, "Existem apenas dois titulares começando com as iniciais 'jo'" );

		filtro.setTitular( "*" );
		contas = contaService.filtra( filtro );
		assertEquals( contas.size(), 4, "Deveriam ter sido filtradas todas as contas." );
		
		filtro.setTitular( "123000123" );
		contas = contaService.filtra( filtro );
		assertTrue( contas.isEmpty(), "Não deveria ter encontrado o titular." );		
	}
	
	@Test
	@ContaBD	
	public void filtraDelete() {
		try {
			ContaResponse joseConta = contaService.getByUsername( "jose" );
			ContaResponse joaoConta = contaService.getByUsername( "joao" );
			ContaResponse mariaConta = contaService.getByUsername( "maria" );
			ContaResponse carlosConta = contaService.getByUsername( "carlos" );
						
			contaService.deleta( joaoConta.getId() );
			contaService.deleta( carlosConta.getId() );
			contaService.deleta( joseConta.getId() );
			
			try {			
				contaService.deleta( -1L );
				fail( "Não deveria ser possível deletar por um ID negativo." );
			} catch ( ServiceException e ) {
				
			}
			
			contaService.deleta( mariaConta.getId() );
			
			ContaFiltroRequest filtro = new ContaFiltroRequest();
			filtro.setTitular( "*" );
			
			List<ContaResponse> contas = contaService.filtra( filtro );
			assertTrue( contas.isEmpty(), "Deveriam ter sido removidas todas as contas." );
		} catch ( ServiceException e ) {
			e.printStackTrace();
		}
	}
	
}

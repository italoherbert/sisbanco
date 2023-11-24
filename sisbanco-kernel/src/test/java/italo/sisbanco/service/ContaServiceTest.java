package italo.sisbanco.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import italo.sisbanco.exception.ServiceException;
import italo.sisbanco.model.request.conta.ContaSaveRequest;
import italo.sisbanco.model.response.conta.ContaResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContaServiceTest {

	@Autowired
	private ContaService contaService;
		
	@Test
	@Sql("/data/data.sql")
	@Sql(scripts = {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
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
	@Sql("/data/data.sql")	
	@Sql(scripts = {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
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
	
}

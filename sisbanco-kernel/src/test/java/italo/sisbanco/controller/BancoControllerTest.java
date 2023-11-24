
package italo.sisbanco.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.model.request.conta.ValorRequest;
import italo.sisbanco.service.BancoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BancoControllerTest {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private BancoService bancoService;
	
	@MockBean
	private KeycloakMicroserviceIntegration keycloak;
						
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity())
				.build();
				
	}				
	
	@Test
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( id, titular, username, saldo, credito ) values ( 1, 'joao', 'joao', 0, 0 )"	
	} )
	@Sql(scripts= {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username="cliente", authorities = { "contaDonoWRITE" } ) 
	public void testCreditar() {		
		int contaId = 1;
		double valor = 300;
		
		try {
			ValorRequest credito = new ValorRequest();
			credito.setValor( valor );
								
			mockMvc.perform( 
					post("/api/banco/depositar/"+contaId )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( credito ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );			
		} catch ( Exception e ) {
			e.printStackTrace();
		}		
	}
	
	@Test
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( id, titular, username, saldo, credito ) values ( 1, 'joao', 'joao', 0, 0 )"	
	} )
	@Sql(scripts= {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username="cliente", authorities = { "contaDonoWRITE" } ) 
	public void testDebitar() {		
		int contaId = 1;
		double valor = 300;
		
		try {
			ValorRequest debito = new ValorRequest();
			debito.setValor( valor );
												
			mockMvc.perform( 
					post("/api/banco/sacar/"+contaId )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( debito ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );			
		} catch ( Exception e ) {
			e.printStackTrace();
		}		
	}
	
	@Test		
	@Sql("/data/data.sql")
	@Sql(statements = {
		"insert into conta ( id, titular, username, saldo, credito ) values ( 1, 'joao', 'joao', 0, 0 )",	
		"insert into conta ( id, titular, username, saldo, credito ) values ( 2, 'maria', 'maria', 0, 0 )"	
	} )
	@Sql(scripts= {"/data/drops.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)	
	@WithMockUser(username="cliente", authorities = { "contaDonoWRITE" } ) 
	public void testTransferir() {		
		int origContaId = 1;
		int destContaId = 2;
		
		double valor = 300;
		
		try {
			ValorRequest transferencia = new ValorRequest();
			transferencia.setValor( valor );
							
			mockMvc.perform( 
					post("/api/banco/transferir/orig/"+origContaId+"/dest/"+destContaId )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( transferencia ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );			
		} catch ( Exception e ) {
			e.printStackTrace();
		}		
	}			
				
}

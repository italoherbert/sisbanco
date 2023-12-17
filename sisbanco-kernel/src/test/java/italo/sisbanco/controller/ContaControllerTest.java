package italo.sisbanco.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.MockedFeignClientsTestConfiguration;
import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.rabbitmq.MockedRabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.integration.model.UserSaveRequest;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;
import italo.sisbanco.kernel.service.ContaService;

@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class ContaControllerTest {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired		
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private ContaService contaService;
		
	@MockBean
	private OperTransacaoCacheRepository transacaoCacheRepository;
			
	@MockBean
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity() )
				.build();								
	}
	
	@Test
	@ContaBD
	@WithMockUser(username = "joao", authorities = { "contaDonoWRITE" })
	public void testComPermissaoSeUsuarioDono() {
		try {
			long JOAO_CONTA_ID = 1;
			long JOSE_CONTA_ID = 2;
			
			UserSaveRequest user = new UserSaveRequest();
			user.setUsername( "pereira" );
			
			ContaSaveRequest req = new ContaSaveRequest();
			req.setTitular( "pereira" );
			req.setUser( user ); 
						
			mockMvc.perform( 
					put( "/api/kernel/contas/"+JOAO_CONTA_ID )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( req ) ) )
				.andDo( print() )
					.andExpect( status().isOk() ); 
			
			mockMvc.perform( 
					put( "/api/kernel/contas/"+JOSE_CONTA_ID )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( req ) ) )
				.andDo( print() )
					.andExpect( status().is( 403 ) );
			
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username = "cliente", authorities = { "contaWRITE" })
	private void testComPermissaoSeUsuarioFuncionario() {
		try {
			long JOAO_CONTA_ID = 1;
			long JOSE_CONTA_ID = 2;
			
			UserSaveRequest user = new UserSaveRequest();
			user.setUsername( "pereira" );
			
			ContaSaveRequest req = new ContaSaveRequest();
			req.setTitular( "pereira" );
			req.setUser( user ); 
			
			mockMvc.perform( 
					put( "/api/kernel/contas/"+JOAO_CONTA_ID )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( req ) ) )
				.andDo( print() )
					.andExpect( status().isOk() ); 
			
			mockMvc.perform( 
					put( "/api/kernel/contas/"+JOSE_CONTA_ID )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( req ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
			
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username = "cliente", authorities = { "contaREAD" })
	private void testSemPermissaoDeEscrita() {
		try {
			long OUTRA_CONTA_ID = -1l;
			
			UserSaveRequest user = new UserSaveRequest();
			user.setUsername( "pereira" );
			
			ContaSaveRequest req = new ContaSaveRequest();
			req.setTitular( "pereira" );
			req.setUser( user ); 
						
			mockMvc.perform( 
					put( "/api/kernel/contas/"+OUTRA_CONTA_ID )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( req ) ) )
				.andDo( print() )
					.andExpect( status().is( 403 ) ); 						
		} catch ( ErrorException e ) {
			e.printStackTrace();
			fail( e.getErrorChave() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@WithMockUser( username="cliente", authorities = {"contaWRITE"} )
	public void registraContaTest() {
		try {												
			UserSaveRequest user = new UserSaveRequest();
			
			ContaSaveRequest conta = new ContaSaveRequest();
			conta.setTitular( "italo" );
			conta.setUser( user );
																			
			mockMvc.perform( 
				post("/api/kernel/contas")
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( conta ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="joao", authorities = {"contaWRITE"} )
	public void alteraContaTest() {
		Long uid = 1L;
		try {															
			ContaSaveRequest conta = new ContaSaveRequest();
			conta.setTitular( "italo" );
												
			mockMvc.perform( 
				put("/api/kernel/contas/"+uid )
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( conta ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="joao", authorities = {"contaREAD"} )
	public void getContaTest() {
		Long uid = 1L;
		try {																														
			mockMvc.perform( 
				get("/api/kernel/contas/"+uid) )
					.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaREAD"} )
	public void filtraContaTest() {
		try {						 			
			mockMvc.perform( 
					get("/api/kernel/contas?titular=*") )				
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaDELETE"} )
	public void deletaContaTest() {
		Long uid = -1L;
		try {								
			mockMvc.perform( 
				delete("/api/kernel/contas/"+uid ) )								
					.andDo( print() )
					.andExpect( status().isOk() );			 
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
}

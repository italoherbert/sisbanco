package italo.sisbanco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.annotation.ContaBD;
import italo.sisbanco.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.integration.model.UserCreated;
import italo.sisbanco.integration.model.UserSaveRequest;
import italo.sisbanco.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.model.request.conta.ContaSaveRequest;
import italo.sisbanco.service.ContaService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContaControllerTest {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private ContaService contaService;
	
	@MockBean
	private KeycloakMicroserviceIntegration keycloak;
						
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity() )
				.build();								
	}
	
	@Test
	@WithMockUser( username="cliente", authorities = {"contaWRITE"} )
	public void registraContaTest() {
		try {												
			UserSaveRequest user = new UserSaveRequest();
			
			ContaSaveRequest conta = new ContaSaveRequest();
			conta.setTitular( "italo" );
			conta.setUser( user );
			
			UserCreated created = mock( UserCreated.class );		
			ResponseEntity<UserCreated> userCreatedEntity = ResponseEntity.ok( created );
											
			when( keycloak.registraUser( any( UserSaveRequest.class ), anyString() ) ).thenReturn( userCreatedEntity );					
												
			mockMvc.perform( 
				post("/api/conta/registra")
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( conta ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaWRITE"} )
	public void alteraContaTest() {
		try {															
			ContaSaveRequest conta = new ContaSaveRequest();
			conta.setTitular( "italo" );
												
			mockMvc.perform( 
				put("/api/conta/altera/1")
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( conta ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaREAD"} )
	public void getContaTest() {
		try {																														
			mockMvc.perform( 
				get("/api/conta/get/1") )
					.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaREAD"} )
	public void filtraContaTest() {
		try {			
			ContaFiltroRequest filtro = new ContaFiltroRequest();
			filtro.setTitular( "*" ); 
			
			mockMvc.perform( 
				post("/api/conta/filtra")
					.contentType(MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( filtro ) ) )				
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser( username="cliente", authorities = {"contaDELETE"} )
	public void deletaContaTest() {
		try {								
			mockMvc.perform( 
				delete("/api/conta/deleta/1") )								
					.andDo( print() )
					.andExpect( status().isOk() );			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
}

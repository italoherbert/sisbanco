
package italo.sisbanco.controller;

import static org.junit.jupiter.api.Assertions.fail;
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
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;
import italo.sisbanco.kernel.service.OperacaoService;

@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class OperacaoControllerTest {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private OperacaoService bancoService;
	
	@MockBean
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@MockBean
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;	
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity())
				.build();		
	}				
		
	@Test	
	@ContaBD
	@WithMockUser(username="maria", authorities = { "contaDonoWRITE" } ) 
	public void testCreditar() {		
		this.creditar( 3, 200 );
		this.creditar( 1, 403 );
		this.creditar( 2, 403 );
		this.creditar( 4, 403 );
		
		this.debitar( 3, 200 );
		this.debitar( 1, 403 );
		this.debitar( 2, 403 );
		this.debitar( 4, 403 );		
		
		this.transferir( 3, 200 ); 	
		this.transferir( 1, 403 ); 
		this.transferir( 2, 403 );
		this.transferir( 4, 403 );
	}			
				
	public void creditar( int contaId, int expectedStatus ) {		
		double valor = 300;
		
		try {
			ValorRequest credito = new ValorRequest();
			credito.setValor( valor );
								
			mockMvc.perform( 
					post("/api/kernel/operacoes/contas/"+contaId+"/depositar" )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( credito ) ) )
				.andDo( print() )
					.andExpect( status().is( expectedStatus ) );			
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}		
	}
	
	public void debitar( int contaId, int expectedStatus ) {		
		double valor = 300;
		
		try {
			ValorRequest debito = new ValorRequest();
			debito.setValor( valor );
												
			mockMvc.perform( 
					post("/api/kernel/operacoes/contas/"+contaId+"/sacar" )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( debito ) ) )
				.andDo( print() )
					.andExpect( status().is( expectedStatus) );			
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}		
	}
	
	public void transferir( int origContaId, int expectedStatus ) {		
		int destContaId = 2;
		
		double valor = 300;
		
		try {
			ValorRequest transferencia = new ValorRequest();
			transferencia.setValor( valor );
							
			mockMvc.perform( 
					post("/api/kernel/operacoes/contas/orig/"+origContaId+"/dest/"+destContaId+"/transferir" )
						.contentType(MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( transferencia ) ) )
				.andDo( print() )
					.andExpect( status().is( expectedStatus ) );			
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}		
	}
	
}

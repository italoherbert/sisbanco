package italo.sisbanco.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import italo.sisbanco.ext.openfeign.FeignClientsTestConfiguration;
import italo.sisbanco.ext.postgresql.ContaBD;
import italo.sisbanco.ext.rabbitmq.RabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;
import italo.sisbanco.kernel.service.OperacaoPendenteCacheService;

@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({
	RabbitMQTestConfiguration.class, 
	FeignClientsTestConfiguration.class
})
public class OperacaoPendenteControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private OperacaoPendenteCacheService operacaoPendenteCacheService;
	
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
	@WithMockUser(username="cliente", authorities = { "cacheOperacoesPendentesALL"})
	public void testExecuta() {		
		try {
			mockMvc.perform( 
					get( "/api/kernel/operacoes/pendentes/-1/exec" ) )
				.andDo( print() )
				.andExpect( status().isOk() ); 
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser(username="cliente", authorities = { "cacheOperacoesPendentesALL"})
	public void testGet() {		
		try {
			mockMvc.perform( 
					get( "/api/kernel/operacoes/pendentes/-1" ) )
				.andDo( print() )
				.andExpect( status().isOk() ); 
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@ContaBD
	@WithMockUser(username="cliente", authorities = { "cacheOperacoesPendentesALL"})
	public void testLista() {		
		try {
			mockMvc.perform( 
					get( "/api/kernel/operacoes/pendentes/contas/-1/lista" ) )
				.andDo( print() )
				.andExpect( status().isOk() ); 
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
}
